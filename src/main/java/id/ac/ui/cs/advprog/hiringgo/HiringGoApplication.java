package id.ac.ui.cs.advprog.hiringgo;

import io.github.cdimascio.dotenv.Dotenv;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class HiringGoApplication {

    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();

        String datasourceUrl = dotenv.get("SPRING_DATASOURCE_URL");
        String datasourceUsername = dotenv.get("SPRING_DATASOURCE_USERNAME");
        String datasourcePassword = dotenv.get("SPRING_DATASOURCE_PASSWORD");

        if (datasourceUrl != null) {
            System.setProperty("SPRING_DATASOURCE_URL", datasourceUrl);
        } else {
            log.warn("SPRING_DATASOURCE_URL is missing in .env");
        }

        if (datasourceUsername != null) {
            System.setProperty("SPRING_DATASOURCE_USERNAME", datasourceUsername);
        } else {
            log.warn("SPRING_DATASOURCE_USERNAME is missing in .env");
        }

        if (datasourcePassword != null) {
            System.setProperty("SPRING_DATASOURCE_PASSWORD", datasourcePassword);
        } else {
            System.out.println("SPRING_DATASOURCE_PASSWORD is missing in .env");
            log.warn("SPRING_DATASOURCE_PASSWORD is missing in .env");
        }

        SpringApplication.run(HiringGoApplication.class, args);
    }

}
