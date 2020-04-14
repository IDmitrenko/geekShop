package ru.geekbrains.supershop.services;

import org.springframework.stereotype.Service;
import ru.geekbrains.supershop.persistence.pojo.Mail;

@Service
public interface MailService {

    void sendEmail(Mail mail);
}