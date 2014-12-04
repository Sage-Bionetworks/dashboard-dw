package org.sagebionetworks.dashboard;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {

    public static void main(String[] args) {

        @SuppressWarnings("resource")
        final ConfigurableApplicationContext context = new ClassPathXmlApplicationContext("/META-INF/spring/app-context.xml");
        context.registerShutdownHook();
        context.start();
    }
}
