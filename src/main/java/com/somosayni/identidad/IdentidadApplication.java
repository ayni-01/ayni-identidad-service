package com.somosayni.identidad;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.somosayni.identidad", "com.somosayni.shared"})
public class IdentidadApplication {
    public static void main(String[] args) {
        SpringApplication.run(IdentidadApplication.class, args);
    }
}
