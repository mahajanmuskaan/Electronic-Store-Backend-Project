package com.springboot.electronicstore.services.serviceImplementations;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.springboot.electronicstore.exceptions.customExceptionhandlers.BadApiRequest;
import com.springboot.electronicstore.services.serviceInterfaces.FileService;

/**
 * Service implementation for file-related operations.
 */
@Service
public class FileServiceImpl implements FileService {

    /**
     * Uploads a file to the specified path.
     * 
     * @param file         The file to upload.
     * @param relativePath The relative path within the project to save the file.
     * @return The filename with extension of the uploaded file.
     * @throws IOException If an I/O error occurs.
     */
    @Override
    public String uploadFile(MultipartFile file, String relativePath) throws IOException {
        // Combine the project root directory with the relative path
        Path directoryPath = Paths.get(System.getProperty("user.dir"), relativePath);

        // Create the directory if it doesn't exist
        if (!Files.exists(directoryPath)) {
            Files.createDirectories(directoryPath);
        }

        // Get the original filename
        String fileOriginalName = file.getOriginalFilename();

        // Generate a unique filename
        String filename = UUID.randomUUID().toString();

        // Extract the file extension
        String extension = fileOriginalName.substring(fileOriginalName.lastIndexOf("."));

        // Create the full filename with the extension
        String fileNameWithExtension = filename + extension;

        // Construct the full path with the filename
        Path fullFilePathWithExtension = directoryPath.resolve(fileNameWithExtension);

        // Check if the file has a supported extension
        if (extension.equalsIgnoreCase(".png") || extension.equalsIgnoreCase(".jpg")
                || extension.equalsIgnoreCase(".jpeg")) {

            // Save the file
            Files.copy(file.getInputStream(), fullFilePathWithExtension);
            return fileNameWithExtension;

        } else {
            // Throw an exception if the file extension is not supported
            throw new BadApiRequest("File with this " + extension + " is not supported!!");
        }
    }

    /**
     * Retrieves a resource (file) from the specified path as an InputStream.
     * 
     * @param path     The path to the directory where the file is located.
     * @param filename The filename of the resource to retrieve.
     * @return An InputStream representing the requested file.
     * @throws FileNotFoundException If the file specified by path and filename is not found.
     */
    @Override
    public InputStream getResource(String path, String filename) throws FileNotFoundException {
        // Combine the project root directory with the specified path and filename
        Path directoryPath = Paths.get(System.getProperty("user.dir"), path);
        String fullPath = directoryPath + File.separator + filename;
        
        // Create an InputStream from the file
        InputStream inputStream = new FileInputStream(fullPath);
        return inputStream;
    }
}
