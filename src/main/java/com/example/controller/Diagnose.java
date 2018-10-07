package com.example.controller;

import com.example.service.StorageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.*;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

@Controller
public class Diagnose {

    @Autowired
    StorageService storageService;

    boolean isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");
    String data;

    @GetMapping("/diag/{fid}")
    public ResponseEntity<List<String>> handleDiagnose(@PathVariable String fid) {
        // read associated json using fid
        ObjectMapper mapper = new ObjectMapper();
        PatientData patientData;
        try {
            patientData = mapper.readValue(storageService.getPatientDataFile(fid), PatientData.class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        String symptoms = String.join(" ", patientData.getSymptoms());

        ProcessBuilder builder = new ProcessBuilder();
        if (isWindows) {
            String pythonExe = "C:\\Users\\Administrator\\AppData\\Local\\Programs\\Python\\Python37-32\\python.exe";
            builder.command(pythonExe, "fangYao_util.py", "--symptoms", symptoms);
        } else {
            builder.command("python3", "fangYao_util.py", "--symptoms", symptoms);
        }
        builder.directory(new File("C:\\Users\\Administrator\\IdeaProjects\\ZhongyiServer\\model"));
        try {
            Process process = builder.start();

            //Read out dir output

            {
                InputStream is = process.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);
                String line;

                while ((line = br.readLine()) != null) {
                    data = line;
                }
            }

            {
                // Print error
                InputStream is = process.getErrorStream();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);
                String line;

                while ((line = br.readLine()) != null) {
                    System.out.println(line);
                }
            }

            process.waitFor();

        } catch (IOException e) {
            System.out.println("diagnose IOException");
            e.printStackTrace();
        } catch (InterruptedException e) {
            System.out.println("diagnose InterruptedException");
            e.printStackTrace();
        }

        System.out.println("data" + data);

        return new ResponseEntity(data, HttpStatus.OK);
    }
}


class StreamGobbler implements Runnable {
    private InputStream inputStream;
    private Consumer<String> consumer;

    public StreamGobbler(InputStream inputStream, Consumer<String> consumer) {
        this.inputStream = inputStream;
        this.consumer = consumer;
    }

    @Override
    public void run() {
        try {
            new BufferedReader(new InputStreamReader(inputStream, "utf-8")).lines().forEach(consumer);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}

