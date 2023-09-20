package me.muse.CrezyBackend.utility.transformToDate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class TransformToDate {

    public static LocalDate transformToDate(String dateString) {
        String pattern = "yyyy-MM-dd";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        try {
            LocalDate localDate = LocalDate.parse(dateString, formatter);
            return localDate;
        } catch (DateTimeParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
