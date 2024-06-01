package com.ahia.blog.util;
public class FormValidationUtil {

    /**
     * 验证字符串是否为空
     * @param input 输入的字符串
     * @return 如果输入的字符串为null或者空白，返回true，否则返回false。
     */
    public static boolean isNullOrEmpty(String input) {
        return input == null || input.trim().isEmpty();
    }

    /**
     * 验证字符串的长度是否在指定的区间内
     * @param input 输入的字符串
     * @param min 最小长度
     * @param max 最大长度
     * @return 如果字符串长度在指定范围内，返回true，否则返回false。
     */
    public static boolean isLengthValid(String input, int min, int max) {
        if (input == null) {
            return false;
        }
        int length = input.length();
        return length >= min && length <= max;
    }

    /**
     * 验证是否是有效的电子邮件地址
     * @param email 输入的电子邮件地址
     * @return 如果是有效的电子邮件地址，返回true，否则返回false。
     */
    public static boolean isValidEmail(String email) {
        if (email == null) {
            return false;
        }
        // 简单的电子邮件验证规则
        return email.matches("^[a-zA-Z0-9_.-]+@[a-zA-Z0-9-]+.[a-zA-Z0-9-.]+$");
    }

    /**
     * 验证是否是有效的中国大陆手机号码
     */
    public static boolean isValidChineseMobile(String mobile) {
        if (mobile == null) {
            return false;
        }
        // 中国大陆手机号码验证规则（简单版本）
        return mobile.matches("^((13[0-9])|(14[5-9])|(15([0-3]|[5-9]))|(16[2-9])|(17[0-8])|(18[0-9])|(19[1-9]))\\d{8}$");
    }
}
