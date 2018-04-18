package com.javateer.cipherkey;

import java.util.UUID;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service()
public class CipherKeyServiceImpl implements CipherKeyService {

    private static final Logger logger = LoggerFactory.getLogger(CipherKeyServiceImpl.class);

    /*
     * cipherKey is a string meant to be the ever changing wild card value used when
     * hashing the authentication token to create a signature by the server for the
     * server to know the token has not been tampered with by the client and the
     * server can trust the content of the web token. There is no rule that the
     * value of cipherKey be changed at a certain interval or period. But the more
     * often, the more secure. Every time it's changed (to anything different; a
     * nonsensical scribble of letters, numbers, and spaces is fine), all would-be
     * encryption crackers will have to start over with analyzing the new JWT auth
     * token produced to discover what passphrase would successfully decrypt the
     * token and thus allow the hacker to create an illegitimate token to fool the
     * server into allowing that caller REST API access.
     */
    private String cipherKey = String.valueOf(UUID.randomUUID().toString());

    /*
     * Instead of letting an unauthorized user know they are being denied the cipher key
     * because they are not making their request from a server not on the white list,
     * we presume the unauthorized caller is a bad actor and we confuse them by returning a false key.
     */
    private String decoyKey = String.valueOf(System.currentTimeMillis());

    private final Object lock = new Object();

    @PostConstruct
    public void init() {
        reloadCipherKey();
    }

    @Override
    public String getCipherKey() {
        synchronized (lock) {
            return cipherKey;
        }
    }

    @Override
    public void reloadCipherKey() {
        synchronized (lock) {
            cipherKey = String.valueOf(UUID.randomUUID().toString());
        }
        logger.debug("Cipher key reloaded with a new, different value.");
    }

	@Override
	public String getDecoyKey() {
		return decoyKey;
	}

	@Override
	public void reloadDecoyKey() {
		decoyKey = String.valueOf(System.currentTimeMillis());
	}
}
