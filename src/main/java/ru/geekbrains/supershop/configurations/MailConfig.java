package ru.geekbrains.supershop.configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfig {

    private String email, password, host;
    private int port;

    @Value("${spring.mail.username}")
    public void setEmail(String email) {
        this.email = email;
    }

    @Value("${spring.mail.password}")
    public void setPassword(String password) {
        this.password = password;
    }

    @Value("${spring.mail.host}")
    public void setHost(String host) {
        this.host = host;
    }

    @Value("${spring.mail.port}")
    public void setPort(int port) {
        this.port = port;
    }

    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        mailSender.setPort(port);

        mailSender.setUsername(email);
        mailSender.setPassword(password);

        Properties props = new Properties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        mailSender.setJavaMailProperties(props);
        return mailSender;
    }
}