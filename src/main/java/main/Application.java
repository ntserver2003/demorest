package main;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"controllers","repositories"})
@EntityScan({"models"})
public class Application {
    private static SessionFactory _factory;
    public static void main(String [] args) {
        _factory = new Configuration()
                .configure()
                .buildSessionFactory();
        SpringApplication.run(Application.class, args);
    }
    public static SessionFactory getFactory() { return _factory; }

}
