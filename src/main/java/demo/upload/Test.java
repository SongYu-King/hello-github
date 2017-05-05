package demo.upload;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {

    public static void main(String[] args) {
        String inputText = " slDss试了dd试时ABDpp  十里   蓝山F试　了　　试 ";
//        splitChineseUpperCase(inputText);

        String spaceRegex = "[\\s|\u3000]+"; // 匹配费单词边界的任何空白字符（换页符、换行符、回车符、制表符、垂直制表符）
        String chineseRegex = "[\u4e00-\u9fa5]+"; // 匹配任意一个汉字
        String regex = "(([\u4e00-\u9fa5]+)([\\s|\u3000]+)([\u4e00-\u9fa5]+)){1}"; 
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(inputText);
        while(matcher.find()){
            System.err.println("|" + matcher.group() + "|");
            inputText = inputText.replace(matcher.group(), matcher.group().replaceAll(spaceRegex, ""));
            matcher = pattern.matcher(inputText);
        }
        System.err.println(inputText);
    }

    private static final String spaceRegex = "[\\s|\u3000]+";
    private static final String englishRegex = "[A-Z]+";
    private static final String chineseRegex = "[\u4e00-\u9fa5]+";
    
    public static void splitChineseUpperCase(String inputStr) {
        // 匹配一个大写字母
        Pattern patternUpperCase = Pattern.compile(englishRegex);
        Matcher matcherUpperCase = patternUpperCase.matcher(inputStr);
        while (matcherUpperCase.find()) {
            String matchLetter = matcherUpperCase.group();
            int matchIndex = inputStr.indexOf(matchLetter);
            String prefixMatchLetter = inputStr.substring(matchIndex - 1, matchIndex);
            if (prefixMatchLetter.matches(chineseRegex)) {
                inputStr = inputStr.replace(matchLetter, "\n" + matchLetter);
            }
        }
        System.out.println("分割结果：" + inputStr);
    }

    private static final String newlineRegex = "(\n)+";
    public static void deleteNewline(String inputStr) {
        Pattern patternNewline = Pattern.compile(newlineRegex);
        Matcher matcherNewline = patternNewline.matcher(inputStr);
        while (matcherNewline.find()) {
            System.err.println(matcherNewline.group());
        }
    }
}
