package nl.saxofoonleren.dropitapi.controller;

import nl.saxofoonleren.dropitapi.service.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("files")
@CrossOrigin(origins = "http://localhost:3000")
public class UploadController {

    @Autowired
    FileUploadService fileUploadService;

    @PostMapping
    public void uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        fileUploadService.uploadFile(file);
    }

//    @PostMapping(value = "/upload", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public ResponseEntity<Object> uploadFile(@RequestParam("file") MultipartFile file) {
//        File convertFile = new File(file.getOriginalFilename())
//        return new ResponseEntity<>("File is uploaded successfully", HttpStatus.OK);
//    }
}
