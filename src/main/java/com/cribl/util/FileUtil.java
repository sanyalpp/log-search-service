package com.cribl.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for all file related operations
 */
@Slf4j
@UtilityClass
public class FileUtil {

    public static String copyFileToLocation(final MultipartFile multipartFile,
                                            final String location) {
        final String destination =
                location + FileSystems.getDefault().getSeparator() + multipartFile.getOriginalFilename();
        createLocationIfNotExist(destination);
        // Create FileOutputStream to write data to the file
        try (InputStream inputStream = multipartFile.getInputStream();
             FileOutputStream outputStream = new FileOutputStream(destination)) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            log.error("Error occurred while copying log to {}", destination, e);
            throw new RuntimeException("Error occurred while copying log to :" + destination, e);
        }
        return new File(destination).getPath();
    }

    public static void createLocationIfNotExist(final String location) {
        if (!Files.exists(Paths.get(location))) {
            try {
                FileUtils.createParentDirectories(new File(location));
            } catch (IOException e) {
                log.error("Error occurred while creating parent directories at {}", location, e);
                throw new RuntimeException("Error occurred while creating parent directories at :" + location, e);
            }
        }
    }

    public static List<String> readFile(final String filePath) {
        final List<String> lines = new ArrayList<>();
        try (FileInputStream fileInputStream = new FileInputStream("."
                + FileSystems.getDefault().getSeparator() + filePath);
             InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, StandardCharsets.US_ASCII);
             BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
             String line = "";
             while ((line = bufferedReader.readLine()) != null) {
                 // No need to add empty lines.
                 if (!line.trim().isEmpty()) {
                     lines.add(line);
                }
            }
        } catch (IOException e) {
            log.error("Error occurred while reading log at {}", filePath, e);
            throw new RuntimeException("Error occurred while reading :" + filePath, e);
        }
        return lines;
    }

    public static void writeContentToFile(final String filePath, String fileContent) {
        try {
            Path path = Paths.get("." + FileSystems.getDefault().getSeparator() + filePath);
            // Create parent directories if they do not exist
            Files.createDirectories(path.getParent());
            // Write the file content to the specified file path
            Files.write(path, fileContent.getBytes());
        } catch (IOException e) {
            log.error("Error occurred while saving file {}", filePath, e);
        }
    }

    /**
     * Reads the content of a file and returns it as a string.
     *
     * @param filePath the path of the file to read
     * @return the content of the file as a string
     * @throws IOException if an I/O error occurs reading from the file or a malformed or unmappable byte sequence is read
     */
    public static String readFileAsString(String filePath) throws IOException {
        Path path = Paths.get("." + FileSystems.getDefault().getSeparator() + filePath);
        return new String(Files.readAllBytes(path));
    }
}
