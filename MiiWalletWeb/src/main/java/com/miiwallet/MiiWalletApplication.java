package com.miiwallet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Punto de entrada de MiiWallet. Arranca Tomcat en http://localhost:8080
 */
@SpringBootApplication
public class MiiWalletApplication {

    public static void main(String[] args) {
        SpringApplication.run(MiiWalletApplication.class, args);
    }
}
