package personal.netflix.clone.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import personal.netflix.clone.app.entities.VideoInfo;
import personal.netflix.clone.app.repositories.VideoInfoRepository;
import personal.netflix.clone.app.services.VideoInfoService;

@SpringBootApplication
public class AppApplication {

	public static void main(String[] args) {
		SpringApplication.run(AppApplication.class, args);
	}

}
