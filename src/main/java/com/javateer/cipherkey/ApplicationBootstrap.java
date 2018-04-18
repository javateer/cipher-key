package com.javateer.cipherkey;

import java.io.IOException;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

public class ApplicationBootstrap {

	public static void main(String[] args) throws IOException {
		@SuppressWarnings("resource")
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
		context.register(AppConfig.class);
		context.refresh();
		((AbstractApplicationContext) context).registerShutdownHook();
	}
}
