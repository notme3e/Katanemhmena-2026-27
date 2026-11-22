package gr.hua.dit.petCareAthens;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
@SpringBootApplication
public class PetCareAthensApplication {

	public static void main(String[] args) {
		SpringApplication.run(PetCareAthensApplication.class, args);
	}

}
