package com.javateer.cipherkey;

import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ClientWhitelist {

    private static final Logger logger = LoggerFactory.getLogger(ClientWhitelist.class);

	@Inject
	private ConfigConstants configConstants;

	private 	Set<String> whitelist = new HashSet<>();

	@PostConstruct
	public void init() {
		String ipAddresses = configConstants.getWhiteListedServerIps();
		StringTokenizer st = new StringTokenizer(ipAddresses);
		while(st.hasMoreTokens()) {
			String ipAddress = st.nextToken();
			whitelist.add(ipAddress);
			logger.debug("Added IP {} to the whitelist.", ipAddress);
		}
	}

	public boolean isWhitelisted(String ipAddress) {
		return whitelist.contains(ipAddress);
	}
}
