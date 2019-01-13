package ru.omnicomm.santa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
public class SantaApplication {

    public static void main(String[] args) {
        SpringApplication.run(SantaApplication.class, args);
    }
}
