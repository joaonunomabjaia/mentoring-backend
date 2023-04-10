package mz.org.fgh.mentoring.util;

import jakarta.inject.Singleton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Singleton
public class Utilities {

    public static boolean stringHasValue(String string){
        return string != null && !string.isEmpty() && string.trim().length() > 0;
    }

    public static String ensureXCaractersOnNumber(long number, int x){
        String formatedNumber = "";
        int numberOfCharacterToIncrise = 0;

        formatedNumber = number + "";

        numberOfCharacterToIncrise = x - formatedNumber.length();

        for(int i = 0; i < numberOfCharacterToIncrise; i++) formatedNumber = "0" + formatedNumber;

        return formatedNumber;
    }

    public static String concatStrings(String currentString, String toConcant, String scapeStr){
        if (!stringHasValue(currentString)) return toConcant;

        if (!stringHasValue(toConcant)) return currentString;

        return currentString + scapeStr+ toConcant;
    }

    public static boolean isStringIn(String value, String... inValues){
        if (inValues == null || value == null) return false;

        for (String str : inValues){
            if (value.equals(str)) return true;
        }

        return false;
    }

    public static boolean listHasElements(ArrayList<?> list){
        return list != null && !list.isEmpty() && list.size() > 0;
    }

    public static <T extends Object> T findOnArray(List<T> list, T toFind){
        for (T o : list) {
            if (o.equals(toFind)) return o;
        }
        return null;
    }

    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }

    public static String generateUUID () {
        return UUID.randomUUID().toString();
    }

    public static String garantirXCaracterOnNumber(long number, int x){
        String formatedNumber = "";
        int numberOfCharacterToIncrise = 0;

        formatedNumber = number + "";

        numberOfCharacterToIncrise = x - formatedNumber.length();

        for(int i = 0; i < numberOfCharacterToIncrise; i++) formatedNumber = "0" + formatedNumber;

        return formatedNumber;
    }

    public static String formatToYYYYMMDD (Date date ) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        String strDate = dateFormat.format(date);

        return strDate;
    }
}
