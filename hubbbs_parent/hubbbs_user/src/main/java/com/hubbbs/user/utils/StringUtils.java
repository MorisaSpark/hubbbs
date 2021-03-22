package com.hubbbs.user.utils;

import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 * User: HB
 * Date: 2019/4/8
 *
 * @author HB
 * To change this template use File | Settings | File Templates.
 */
@Component
public class StringUtils {
    public static String toEpochMilli() {
        return "" + Instant.now().toEpochMilli();
    }

    /**
     * @return java.lang.Boolean
     * @desc 验证手机号
     * @method regexUsername
     * @params [username]
     * @author hubdir
     * @date 2019/4/24 15:12
     */
    public static Boolean regexUsername(String username) {
        // 要验证的字符串
        // 手机验证规则
        String regEx = "[1][3,4,5,7,8][0-9]{9}";
        // 编译正则表达式
        Pattern pattern = Pattern.compile(regEx);
        // 忽略大小写的写法
        // Pattern pat = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(username);
        // 字符串是否与正则表达式相匹配
        return matcher.matches();
    }

    /**
     * @return java.lang.Boolean
     * @desc 判断传入的毫秒值是否是同一天
     * @method isSameDay
     * @params [milli]
     * @author hubdir
     * @date 2019/4/24 15:31
     */
    public static Boolean isSameDay(String aTime, String bTime) {
        LocalDateTime aNow = LocalDateTime.ofEpochSecond(Long.parseLong(aTime) / 1000, 0, ZoneOffset.ofHours(0));
        int year = aNow.getYear();
        int dayOfYear = aNow.getDayOfYear();

        LocalDateTime bNow = LocalDateTime.ofEpochSecond(Long.parseLong(bTime) / 1000, 0, ZoneOffset.ofHours(0));
        int yearMilli = bNow.getYear();
        int dayOfYearMilli = bNow.getDayOfYear();

        return (year == yearMilli) && (dayOfYear == dayOfYearMilli);
    }

    /**
     * @return java.lang.Boolean
     * @desc 判断登录时间是今日的第一次登录（上次登录的时间不在今天&&这次登录的时间是今天）
     * @method isCheckInLogin
     * @params [loginTime, loginTimeB]
     * @author hubdir
     * @date 2019/4/24 15:40
     */
    public static Boolean isCheckInLogin(String loginTime, String loginTimeB) {
        String now = StringUtils.toEpochMilli();
        return (!isSameDay(loginTimeB, now) && isSameDay(loginTime, now));
    }

}
