package id.ac.ui.cs.advprog.hiringgo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HiringGoApplication {

    public static void main(String[] args) {
        EnvironmentConfig environmentConfig = new EnvironmentConfig();
        environmentConfig.loadEnvVariables();
        SpringApplication.run(HiringGoApplication.class, args);
    }
}
