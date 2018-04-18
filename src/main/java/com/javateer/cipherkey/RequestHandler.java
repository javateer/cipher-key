package com.javateer.cipherkey;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Controller;

@Controller
public class RequestHandler {

    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

	@Inject
	ApplicationContext applicationContext;

	@Inject
	private ConfigConstants configConstants;

	@Inject
	private CipherKeyService cipherKeyService;

	@Inject
	ClientWhitelist clientWhitelist;

	private ServerSocket serverSocket;

	@PostConstruct
	public void startup() {
		Executors.newSingleThreadExecutor().submit(new Runnable() {
			@Override
			public void run() {
				reserveListeningPort();
				serviceInboundRequests();
			}
		});
	}

	protected void reserveListeningPort() {
		int port = configConstants.getListeningPortNumber();
		try {
			serverSocket = new ServerSocket(port);
			logger.info("Listening for inbound requests on port {} ....", port);
		}
		catch (IOException e) {
			logger.error("Failed to bind to listening port {} at application startup.\n{}", port, e);
			((ConfigurableApplicationContext) applicationContext).close();
		}
	}

	protected void serviceInboundRequests() {
		while (true) {
			String clientAddress = "";
			try {
				Socket socket = serverSocket.accept();
				clientAddress = getIpAddress(socket);
				if(clientWhitelist.isWhitelisted(clientAddress)) {
					String currentKey = cipherKeyService.getCipherKey();
					sendResponse(socket, currentKey);
				}
				else {
					String decoy = cipherKeyService.getDecoyKey();
					sendResponse(socket, decoy);
				}
				socket.close();
			}
			catch(IOException e) {
				logger.error("Failed to service request from client {}\n{},", clientAddress, e);
			}
		}
	}

	protected String getIpAddress(Socket socket) {
		SocketAddress remoteSocketAddress = socket.getRemoteSocketAddress();
		String ipAddress = remoteSocketAddress.toString(); //a format example is "/127.0.0.1:5678"
		ipAddress = ipAddress.substring(1, ipAddress.indexOf(':'));
		return ipAddress;
	}

	protected void sendResponse(Socket socket, String key) throws UnsupportedEncodingException, IOException {
		socket.getOutputStream().write((key + "\r\n").getBytes("UTF-8"));
	}

	@PreDestroy
	public void cleanUp() throws IOException {
		serverSocket.close();
	}
}
