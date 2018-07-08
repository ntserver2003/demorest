package main;
import models.*;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"controllers"})
public class Application {
    private static SessionFactory _factory;
    public static void main(String [] args) {
        _factory = new Configuration()
                //.addPackage("models.*")
                .addAnnotatedClass(Document.class)
                .configure()
                .buildSessionFactory();
        SpringApplication.run(Application.class, args);
    }
    public static SessionFactory getFactory() { return _factory; }

}
