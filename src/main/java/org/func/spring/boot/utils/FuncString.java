package org.func.spring.boot.utils;

import org.func.spring.boot.type.FuncStringConstantPool;
import org.springframework.util.Assert;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Yiur
 */
public final class FuncString implements FuncStringConstantPool {

    @SuppressWarnings("All")
    public static String format(String input, String... args) {
        return formatInit(input, args);
    }

    @SuppressWarnings("All")
    public static String formatTranslate(String input, String... args) {
        for (int i = 0; i < args.length; i++) {
            args[i] = args[i].replaceAll("\\\\", "/");
        }
        return formatInit(input, args);
    }

    public static String format(String input, Object... args) {
        return formatInit(input, args);
    }

    private static String formatInit(String input, Object... args) {
        String result = input;
        Assert.isTrue(args.length == matcherLength(STRING_FORMAT, result), "The current parameter is inconsistent with the number of matching characters");
        for (Object arg : args) {
            result = result.replaceFirst(STRING_FORMAT, arg.toString());
        }
        return result;
    }

    public static String matcher(String regex, String input) {
        String result = EMPTY;
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        while (matcher.find()) {
            result = matcher.group();
        }
        return result;
    }

    public static String replace(String expression, Map<String, Object> constraint) {
        Assert.notNull(expression, "expression is not null!");
        List<String> matchers = matchers(STRING_REPLACE, expression);
        String result = expression;
        for (String matcher : matchers) {
            result = result.replace(matcher, FuncScriptEngine.eval(matcher.substring(2, matcher.length() - 1), (LinkedHashMap<String, Object>) constraint).toString());
        }
        return result;
    }

    public static List<String> matchers(String regex, String input) {
        List<String> result = new ArrayList<>();
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        while (matcher.find()) {
            result.add(matcher.group());
        }
        return result;
    }

    public static int matcherLength(String regex, String input) {
        int result = 0;
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        while (matcher.find()) {
            result++;
        }
        return result;
    }

    public static String removeParentheses(String input) {
        int indexOf = input.indexOf("(") + 1;
        int lastIndexOf = input.lastIndexOf(")");
        return input.substring(indexOf, lastIndexOf);
    }

    public static String charLinkFormat(List<String> input, String charLink) {
        if (input.size() == 0) {
            return EMPTY;
        }
        StringBuilder sb = new StringBuilder();
        Object[] inputs = input.toArray();
        for (int i = 0; i < inputs.length - 1; i++) {
            sb.append(inputs[i]).append(charLink);
        }
        sb.append(inputs[input.size() - 1]);
        return sb.toString();
    }

    public static String lowercaseFirstLetter(String input) {
        String start = input.substring(0, 1).toLowerCase();
        String end = input.substring(1);
        return start + end;
    }

}
