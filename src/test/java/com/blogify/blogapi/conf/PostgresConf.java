package com.blogify.blogapi.conf;

import com.blogify.blogapi.PojaGenerated;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.testcontainers.containers.PostgreSQLContainer;

@PojaGenerated
public class PostgresConf {

  String flywayTestdataPath = "classpath:/db/testdata";
  private final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:13.9");

  void start() {
    postgres.start();
  }

  void stop() {
    postgres.stop();
  }

  void configureProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", postgres::getJdbcUrl);
    registry.add("spring.datasource.username", postgres::getUsername);
    registry.add("spring.datasource.password", postgres::getPassword);
    registry.add("spring.flyway.locations", () -> "classpath:/db/migration," + flywayTestdataPath);
  }
}
