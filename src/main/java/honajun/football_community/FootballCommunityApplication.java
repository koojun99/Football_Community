package honajun.football_community;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling
@EnableCaching
public class FootballCommunityApplication {

    public static void main(String[] args) {
        // Load .env file before Spring Boot starts
        loadEnvFile();
        SpringApplication.run(FootballCommunityApplication.class, args);
    }

    /**
     * Loads environment variables from .env file into System properties.
     * This allows Spring Boot to access these values via ${VAR_NAME} placeholders.
     */
    private static void loadEnvFile() {
        try {
            java.io.File envFile = new java.io.File(".env");
            if (envFile.exists()) {
                java.util.List<String> lines = java.nio.file.Files.readAllLines(envFile.toPath());

                for (String line : lines) {
                    line = line.trim();
                    // Skip empty lines and comments
                    if (line.isEmpty() || line.startsWith("#")) {
                        continue;
                    }
                    int equalsIndex = line.indexOf('=');
                    if (equalsIndex > 0) {
                        String key = line.substring(0, equalsIndex).trim();
                        String value = line.substring(equalsIndex + 1).trim();
                        // Remove quotes if present
                        if ((value.startsWith("\"") && value.endsWith("\"")) ||
                                (value.startsWith("'") && value.endsWith("'"))) {
                            value = value.substring(1, value.length() - 1);
                        }
                        if (!key.isEmpty() && !value.isEmpty() && System.getProperty(key) == null) {
                            System.setProperty(key, value);
                        }
                    }
                }
            }
        } catch (Exception e) {
            // Ignore if .env file doesn't exist or can't be read
        }
    }

}
