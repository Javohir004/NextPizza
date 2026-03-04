package uz.jvh.nextpizza.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.jvh.nextpizza.enomerator.RequestType;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

@Service
@Slf4j
public class FileStorageService {

    @Value("${supabase.url}")
    private String supabaseUrl;

    @Value("${supabase.secret-key}")
    private String supabaseKey;

    @Value("${supabase.bucket}")
    private String bucket;

    public String saveFile(MultipartFile file, RequestType requestType) throws IOException {

        String folder = requestType == RequestType.DRINK ? "drinks" : "pizzas";
        String fileName = UUID.randomUUID() + ".png";
        String path = folder + "/" + fileName;

        // Supabase Storage API ga yuklash
        String uploadUrl = supabaseUrl + "/storage/v1/object/" + bucket + "/" + path;

        HttpURLConnection conn = (HttpURLConnection) new URL(uploadUrl).openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setRequestProperty("Authorization", "Bearer " + supabaseKey);
        conn.setRequestProperty("Content-Type", "image/png");
        conn.setRequestProperty("x-upsert", "true");

        try (OutputStream os = conn.getOutputStream()) {
            os.write(file.getBytes());
        }

        int responseCode = conn.getResponseCode();
        if (responseCode != 200 && responseCode != 201) {
            throw new IOException("Supabase upload failed: " + responseCode);
        }

        // Public URL qaytaradi
        String publicUrl = supabaseUrl + "/storage/v1/object/public/" + bucket + "/" + path;
        log.info("Rasm yuklandi: {}", publicUrl);
        return publicUrl;
    }

    public void deleteFile(String imageUrl, RequestType requestType) {
        if (imageUrl == null || imageUrl.isBlank()) return;

        try {
            // URL dan path ni ajratib olamiz
            String path = imageUrl.substring(imageUrl.indexOf("/object/public/") + 15);
            String deleteUrl = supabaseUrl + "/storage/v1/object/" + path;

            HttpURLConnection conn = (HttpURLConnection) new URL(deleteUrl).openConnection();
            conn.setRequestMethod("DELETE");
            conn.setRequestProperty("Authorization", "Bearer " + supabaseKey);
            conn.getResponseCode();
            log.info("Rasm o'chirildi: {}", path);
        } catch (Exception e) {
            log.warn("Rasmni o'chirishda xatolik: {}", imageUrl, e);
        }
    }
}