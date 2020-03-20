package ru.geekbrains.supershop.services.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class CHelper {

    private static final Pattern UUID_PATTERN =
            Pattern.compile("([a-f0-9]{8}(-[a-f0-9]{4}){4}[a-f0-9]{8})");
    // [0-9a-fA-F]{8}\-[0-9a-fA-F]{4}\-[0-9a-fA-F]{4}\-[0-9a-fA-F]{4}\-[0-9a-fA-F]{12}

    public static boolean parseUUIDString(String uuid) {
        Matcher matcher = UUID_PATTERN.matcher(uuid);
        if (matcher.matches()) {
            return true;
        }
        return false;
    }
}
