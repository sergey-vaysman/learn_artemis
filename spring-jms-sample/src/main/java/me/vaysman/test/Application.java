package me.vaysman.test;

import javax.jms.ConnectionFactory;
import javax.jms.Session;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

/*
  Manual send message to queue:
  header: _type me.vaysman.test.Email
  body: {"to":"info@example.com","body":"Hello"}
*/

@SpringBootApplication
@EnableJms
public class Application {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);

        JmsTemplate jmsTemplate = context.getBean(JmsTemplate.class);
        for (int i = 1; i <= 100; i++) {
            System.out.printf("Sending an email message %d%n", i);
            jmsTemplate.convertAndSend("mailbox", new Email("info@example.com", String.format("Hello %d", i)));
        }


    }

    @Bean
    public JmsListenerContainerFactory<?> myFactory(ConnectionFactory cf, DefaultJmsListenerContainerFactoryConfigurer configurer) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setSessionAcknowledgeMode(Session.CLIENT_ACKNOWLEDGE);

        // This provides all boot's default to this factory, including the message converter
        configurer.configure(factory, cf);

        // You could still override some of Boot's default if necessary.
        return factory;
    }

    // Serialize message content to json usingTextMessage
    @Bean
    public MessageConverter jacksonJmsMessageConverter() {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type");
        return converter;
    }

}
