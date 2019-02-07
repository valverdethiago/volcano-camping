package com.upgrade.volcanocamping;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.rest.RepositoryRestMvcAutoConfiguration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * @author valverde.thiago
 *
 */
@SpringBootApplication(exclude = RepositoryRestMvcAutoConfiguration.class)
@EnableAspectJAutoProxy
public class VolcanoCampingApplication {
  public static void main(String[] args) {
    SpringApplication.run(VolcanoCampingApplication.class, args);
  }
}
