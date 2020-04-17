package ru.geekbrains.supershop.persistence.pojo;

import lombok.Data;

import java.io.File;
import java.util.List;

@Data
public class Mail {

    private String mailFrom;
    private String mailTo;
    private String mailCc;
    private String mailBcc;
    private String mailSubject;
    private String mailContent;
    private String contentType;
    private List<File> attachments;

    public Mail() {
        contentType = "text/plain";
    }
}