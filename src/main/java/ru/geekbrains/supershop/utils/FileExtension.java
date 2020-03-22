package ru.geekbrains.supershop.utils;

import lombok.Getter;

public enum FileExtension {
    PNG("png"),
    JPG("jpg");

    @Getter
    private String name;

    FileExtension(String name) {
        this.name = name;
    }

    public static boolean isExtension(String name) {
       FileExtension[] extensions = FileExtension.values();
        for (FileExtension extension : extensions) {
            if (extension.name == name) {
                return true;
            }
        }
        return false;
    }
}
