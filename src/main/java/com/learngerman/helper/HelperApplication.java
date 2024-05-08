package com.learngerman.helper;

import com.vaadin.flow.component.page.AppShellConfigurator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HelperApplication  implements AppShellConfigurator {

	public static void main(String[] args) {
		SpringApplication.run(HelperApplication.class, args);
	}

}
