package com.rookie.stack.aidocs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.shell.command.annotation.CommandScan;

@CommandScan
@SpringBootApplication
public class AidocsApplication {
    public static void main(String[] args) {
		SpringApplication.run(AidocsApplication.class, args);
	}
}
