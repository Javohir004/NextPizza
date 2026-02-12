package uz.jvh.nextpizza.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.jvh.nextpizza.enomerator.RequestType;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageService {

    private final String uploadDirPizza = "uploads/pizzas/";
    private final String uploadDirDrink = "uploads/drinks/";

    public String saveFile(MultipartFile file , RequestType requestType) throws IOException {

        Path uploadPath = null;

        if(requestType.equals(RequestType.DRINK)){
             uploadPath = Paths.get(uploadDirDrink);
        }else {
         uploadPath = Paths.get(uploadDirPizza);
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
}