package com.springboot.electronicstore.services.serviceInterfaces;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {
	
	String uploadFile(MultipartFile file, String path) throws IOException;
	
	InputStream getResource(String path, String filename) throws FileNotFoundException;

}
