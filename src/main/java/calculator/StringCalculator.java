package calculator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringCalculator {

    public int add(String text) {
        if (isBlank(text)) {
            return 0;
        }

        int result = 0;
        String[] splitString = split(text);
        for (String s : splitString) {
            int parseInt = toInt(s);
            result += parseInt;
        }
        return result;
    }

    private boolean isBlank(String text) {
        return text == null || text.isEmpty();
    }

    private String[] split(String text) {
        Matcher matcher = Pattern.compile("//(.)\n(.*)").matcher(text);
        if (matcher.find()) {
            String customDelimiter = matcher.group(1);
            return matcher.group(2).split(customDelimiter);
        }
        return text.split("[,:]");
    }

    private int toInt(String s) {
        int number = Integer.parseInt(s);
        if (number < 0) {
            throw new RuntimeException();
        }
        return number;
    }
}
