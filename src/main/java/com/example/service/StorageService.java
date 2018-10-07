package com.example.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class StorageService {

    private Path rootLocation = Paths.get("upload-data");
    private Path patientDataLocation = rootLocation.resolve("patient-data");
    private Path imageDataLocation = rootLocation.resolve("image-data");
    private ObjectMapper mapper = new ObjectMapper();

    public StorageService() throws Exception{
        try {
            Files.createDirectories(rootLocation);
            Files.createDirectories(patientDataLocation);
            Files.createDirectories(imageDataLocation);
        }
        catch (IOException e) {
            throw new Exception("Could not initialize storage", e);
        }
    }

    public void store(MultipartFile file) throws Exception {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            if (file.isEmpty()) {
                throw new Exception("Failed to store empty file " + filename);
            }
            if (filename.contains("..")) {
                // This is a security check
                throw new Exception(
                        "Cannot store file with relative path outside current directory "
                                + filename);
            }
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, imageDataLocation.resolve(filename),
                        StandardCopyOption.REPLACE_EXISTING);
            }
        }
        catch (IOException e) {
            throw new Exception("Failed to store file " + filename, e);
        }
    }

    public void dumpJson(String fname, Object json) throws IOException {
        if (!fname.endsWith(".json")) {
            fname += ".json";
        }
        mapper.writeValue(patientDataLocation.resolve(fname).toFile(), json);
    }

    public File getPatientDataFile(String basename) {
        if (!basename.endsWith(".json")) {
            basename += ".json";
        }
        return patientDataLocation.resolve(basename).toFile();
    }
}
