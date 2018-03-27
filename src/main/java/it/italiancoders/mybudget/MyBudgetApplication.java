package it.italiancoders.mybudget;

import it.italiancoders.mybudget.dao.test.TestDao;
import it.italiancoders.mybudget.dao.user.UserDao;
import it.italiancoders.mybudget.model.api.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.PostConstruct;
import java.util.*;

@SpringBootApplication
@EnableAsync
@EnableScheduling
public class MyBudgetApplication {

	@PostConstruct
	void started() {

		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));

	}

	@Bean
	public SimpleAsyncTaskExecutor taskExecutor() {
		return new SimpleAsyncTaskExecutor ();
	}

	public static void main(String[] args) {
		SpringApplication.run(MyBudgetApplication.class, args);
	}

	@Bean
	public CommandLineRunner loadData(UserDao testDao) {
		return (args) -> {
			User ret = testDao.findByUsernameCaseInsensitive("admin");
		};
	}
}
