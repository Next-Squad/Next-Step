package calculator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringCalculator {

    int result = 0;

    int add(String text) {
        if (text.isEmpty()) {
            return 0;
        }

        try {
            String[] split = text.split("[,:]");
            for (String s : split) {
                int parseInt = Integer.parseInt(s);
                positiveNumberCheck(parseInt);
                result += parseInt;
            }
            return result;
        } catch (NumberFormatException e) {
            return customDelimiter(text);
        }
    }

    private void positiveNumberCheck(int parseInt) {
        if (parseInt < 0) {
            throw new RuntimeException();
        }
    }

    private int customDelimiter(String text) {
        Pattern pattern = Pattern.compile("//(.)\n(.*)");
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            String customDelimiter = matcher.group(1);
            String[] split = matcher.group(2).split(customDelimiter);
            for (String s : split) {
                int parseInt = Integer.parseInt(s);
                positiveNumberCheck(parseInt);
                result += parseInt;
            }
        }
        return result;
    }
}
