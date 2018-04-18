package com.javateer.cipherkey;

import javax.inject.Inject;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component()
public class ConfigConstants {

	@Inject
	private Environment env;

	public String getCipherKeyChangeCronSchedule() {
		return env.getProperty("cipherkey.change-cronschedule");
	}

	public String getWhiteListedServerIps() {
		return env.getProperty("cipherkey.whitelisted-servers");
	}

	public Integer getListeningPortNumber() {
		return env.getProperty("cipherkey.listening-port-number", Integer.class);
	}
}
