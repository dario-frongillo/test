package it.italiancoders.mybudget;

import it.italiancoders.mybudget.dao.test.TestDao;
import it.italiancoders.mybudget.dao.user.UserDao;
import it.italiancoders.mybudget.model.api.User;
import it.italiancoders.mybudget.service.account.AccountManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.annotation.PostConstruct;
import java.util.*;

@SpringBootApplication
@EnableAsync
@EnableScheduling
public class MyBudgetApplication {

	@Autowired
	AccountManager accountManager;

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
	public CommandLineRunner loadData(UserDao testDao, AccountManager accountManager) {
		return (args) -> {
			User ret = testDao.findByUsernameCaseInsensitive("admin");
			accountManager.generateAutoMovement(new Date());
		};
	}

	@Scheduled(cron = "0 0 0 * * *",zone = "GMT")
	public void scheduleAutoMovementGen(){
		Date date = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		date = cal.getTime();

		accountManager.generateAutoMovement(date);

	}

}
