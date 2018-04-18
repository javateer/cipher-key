package com.javateer.cipherkey;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import javax.inject.Inject;

import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

@Configuration
@ComponentScan
@PropertySources({
	@PropertySource("classpath:cipherkey.properties")
})
public class AppConfig {

	@Inject
	private ApplicationContext applicationContext;

	@Inject
	private ConfigConstants configConstants;

	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}

	@Bean
	public JobDetail cipherJob() {
		return newJob(CipherKeyJob.class)
				.storeDurably(true)
				.withIdentity("Cipher Job")
				.withDescription("reloading cipher key")
				.build();
	}

	@Bean
	public Trigger cipherTrigger() {
		return newTrigger()
				.startNow()
				.withIdentity("Trigger", "Cipher Trigger")
				.forJob(cipherJob())
				.withSchedule(cronSchedule(configConstants.getCipherKeyChangeCronSchedule()))
				.build();
	}

	@Bean
	public SpringBeanJobFactory springBeanJobFactory() {
		AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
		jobFactory.setApplicationContext(applicationContext);
		return jobFactory;
	}

	@Bean
	public SchedulerFactoryBean scheduler() {
		SchedulerFactoryBean schedulerFactory = new SchedulerFactoryBean();
		schedulerFactory.setConfigLocation(new ClassPathResource("quartz.properties"));
		schedulerFactory.setJobFactory(springBeanJobFactory());
		schedulerFactory.setJobDetails(cipherJob());
		schedulerFactory.setTriggers(cipherTrigger());
		return schedulerFactory;
	}
}
