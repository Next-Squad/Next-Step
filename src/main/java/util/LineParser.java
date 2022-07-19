package util;

public class LineParser {

    public static String parseLine(String line){
        String[] parsedLines = line.split(" ");
        return parsedLines[1];
    }
}
