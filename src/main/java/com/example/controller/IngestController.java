package com.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.example.service.StorageService;

import java.io.IOException;

@Controller
public class IngestController {
    @Autowired
    private StorageService storageService;

    @PostMapping("/ingest")
    public ResponseEntity handleFileUpload(@RequestParam("file") MultipartFile file) {
        try {
            storageService.store(file);
        } catch (Exception e) {
            System.out.println("handleFileUpload: " + e.getMessage());
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/patientdata")
    public ResponseEntity handlePatientDataUpload(@RequestBody PatientData patient) {
        System.out.println("heartRate: " + patient.getHeartRate() + " " + patient.getId());
        try {
            storageService.dumpJson(patient.getId(), patient);
        } catch (IOException e) {
            System.out.println("handlePatientDataUpload: " + e.getMessage());
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity(HttpStatus.OK);
    }
}

class PatientData {
    private String id;
    private int heartRate;

    public void setId(String id) {
        this.id = id;
    }
    public String getId() {
        return id;
    }

    public void setHeartRate(int rate) {
        heartRate = rate;
    }
    public int getHeartRate() {
        return heartRate;
    }
}
