package nl.saxofoonleren.dropitapi.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class FileUploadService {

    public static String uploadDirectory = System.getProperty("user.dir") + "/uploads/";

public void uploadFile(MultipartFile file) throws IOException {
    file.transferTo(new File(uploadDirectory + file.getOriginalFilename()));
}
}
