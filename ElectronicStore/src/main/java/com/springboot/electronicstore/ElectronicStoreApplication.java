package com.springboot.electronicstore;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class ElectronicStoreApplication implements CommandLineRunner {

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public static void main(String[] args) {
		SpringApplication.run(ElectronicStoreApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("Electronic Store Project begins....");
		
//		System.out.println(passwordEncoder.encode("12345678"));
//		System.out.println(passwordEncoder.encode("Kris@202"));
//		System.out.println(passwordEncoder.encode("alpzzz06"));
//		System.out.println(passwordEncoder.encode("trina25@"));
//		System.out.println(passwordEncoder.encode("sonpatrc"));
//		System.out.println(passwordEncoder.encode("liza1234"));
		
		
	}

}
