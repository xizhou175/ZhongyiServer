package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.example.service.StorageService;

@Controller
public class IngestController {
    @Autowired
    private StorageService storageService;

    @PostMapping("/ingest")
    public ResponseEntity handleFileUpload(@RequestParam("file") MultipartFile file,
                                           RedirectAttributes redirectAttributes) {
        try {
            storageService.store(file);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/heartrate")
    public ResponseEntity handleHeartRateUpload(@RequestParam("heartRate") String heartRate) {
        System.out.println("heartRate: " + heartRate);
        return new ResponseEntity(HttpStatus.OK);
    }

}
