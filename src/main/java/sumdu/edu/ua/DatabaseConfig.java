package sumdu.edu.ua;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfig {

    @Bean
    public CommandLineRunner commandLineRunner(DataSource dataSource) {
        return args -> {
            ResourceDatabasePopulator resourceDatabasePopulator = new ResourceDatabasePopulator(false, false, "UTF-8", new ClassPathResource("pgsql_init.sql"));
            resourceDatabasePopulator.execute(dataSource);
        };
    }
}
