package com.example.demo.config;

import java.util.Arrays;

import javax.sql.DataSource;

import org.flywaydb.core.Flyway;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FlywayConfig {
	 @Bean
	    CommandLineRunner flywayDebugRunner(DataSource dataSource) {
	        return args -> {
	            System.out.println("\n=================================");
	            System.out.println("🚀 FLYWAY STARTED");
	            System.out.println("=================================\n");
	            
	            try {
	                Flyway flyway = Flyway.configure()
	                    .dataSource(dataSource)
	                    .locations("classpath:db/migration")
	                    .schemas("auth")
	                    .defaultSchema("auth")
	                    .baselineOnMigrate(true)
	                    .baselineVersion("0")
	                    .validateOnMigrate(true)
	                    .load();
	                
	                System.out.println("📊 Flyway Info:");
	                Arrays.stream(flyway.info().all()).forEach(info -> {
	                    System.out.println("  Version: " + info.getVersion() + 
	                                     " | Description: " + info.getDescription() + 
	                                     " | State: " + info.getState());
	                });
	                
	                System.out.println("\n🔄 Migration starting...");
	                int migrationsApplied = flyway.migrate().migrationsExecuted;
	                System.out.println("✅ " + migrationsApplied + " migration uygulandı!");
	                
	                System.out.println("\n=================================");
	                System.out.println("✅ FLYWAY FINISHED");
	                System.out.println("=================================\n");
	                
	            } catch (Exception e) {
	                System.err.println("\n❌ FLYWAY ERROR:");
	                e.printStackTrace();
	                System.err.println("\n=================================\n");
	            }
	        };
	    }
}
