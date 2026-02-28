package uz.jvh.nextpizza.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.jvh.nextpizza.enomerator.RequestType;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.UUID;

@Service
@Slf4j
public class FileStorageService {

    @Value("${UPLOAD_DIR:uploads}")
    private String uploadDir;

    private String uploadDirPizza() {
        return uploadDir + "/pizzas/";
    }

    private String uploadDirDrink() {
        return uploadDir + "/drinks/";
    }

    public String saveFile(MultipartFile file , RequestType requestType) throws IOException {

        Path uploadPath = null;

        if(requestType.equals(RequestType.DRINK)){
             uploadPath = Paths.get(uploadDirDrink());
        }else {
         uploadPath = Paths.get(uploadDirPizza());
        }

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String fileName = UUID.randomUUID() + ".png";

        Files.copy(file.getInputStream(),
                uploadPath.resolve(fileName),
                StandardCopyOption.REPLACE_EXISTING);

        return fileName;
    }

    public void deleteFile(String fileName, RequestType requestType) {
        if (fileName == null || fileName.isBlank()) return;

        Path uploadPath = requestType.equals(RequestType.DRINK)
                ? Paths.get(uploadDirDrink())
                : Paths.get(uploadDirPizza());

        try {
            Files.deleteIfExists(uploadPath.resolve(fileName));
        } catch (IOException e) {
            log.warn("Faylni o'chirishda xatolik: {}", fileName, e);
        }
    }

}