package com.example.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.*;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

@Controller
public class Diagnose {

    boolean isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");
    String data;
    Consumer<String> consumer = s -> data = s;


    @GetMapping("/diag")
    public ResponseEntity<List<String>> handleDiagnose() {
        ProcessBuilder builder = new ProcessBuilder();
        if (isWindows) {
            builder.command("cmd.exe", "/c", "dir");
        } else {
            builder.command("python3", "fangYao_util.py");
        }
        builder.directory(new File("./model"));
        try {
            Process process = builder.start();
            StreamGobbler streamGobbler =
                    new StreamGobbler(process.getInputStream(), consumer);
            Executors.newSingleThreadExecutor().submit(streamGobbler);

            process.waitFor();

        } catch (IOException e) {

        } catch (InterruptedException e) {

        }

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

