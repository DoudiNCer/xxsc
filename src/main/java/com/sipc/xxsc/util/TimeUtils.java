package com.sipc.xxsc.util;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class TimeUtils {
    /**
     * @apiNote 获取东八区零点的时间戳
     * @return 当天零点的10位时间戳
     */
    public static Long getDayTime() {
        LocalDateTime today_start = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        return today_start.toInstant(ZoneOffset.of("+8")).toEpochMilli() / 1000;
    }
    /**
     * @apiNote 获取当前的10位时间戳（请确保服务器时间正确）
     * @return 十位时间戳
     */
    public static Long getNow(){
        Calendar calendar = Calendar.getInstance();
        return calendar.getTimeInMillis()/1000L;
    }

    /**
     * @apiNote 格式化时间戳
     * @param timeStamp 十位时间戳
     * @return "yyyy/MM/dd E" 的日期
     */
    public static String formatDateTime(Long timeStamp){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd E");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        return simpleDateFormat.format(new Date(timeStamp * 1000));
    }

    /**
     * @apiNote 获取几天前的时间戳
     * @param day 天数
     * @return 东八区几天前的零点的10位时间戳
     */
    public static Long getDayTimeBefore(Integer day) {
        day *= -1;
        LocalDateTime today_start = LocalDateTime.of(LocalDate.now().plusDays(day), LocalTime.MIN);
        return today_start.toInstant(ZoneOffset.of("+8")).toEpochMilli() / 1000;

    }

    /**
     * @apiNote 将时间戳转化为“易读时间”字符串
     * <blockquote><pre>
     * 若时间为当天，展示到分钟，格式为「HH:MM」
     * 若时间为昨天，展示文案「昨天」
     * 若时间为昨天之前，展示到天，格式为「M月D日」
     * 若时间为今年之前，展示到天，格式为「YYYY-MM-DD」
     * </pre></blockquote>
     * @param time 要处理的时间戳（精确到毫秒）
     * @return 处理后的易读时间
     * @author DoudiNCer
     */
    public static String EasyRead(Long time) {

        // 今天
        if (time >= getDayTime()) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+8"));
            return simpleDateFormat.format(new Date(time * 1000L));
        }
        // 昨天
        if (time > getDayTimeBefore(1))
            return "昨天";
        // 今年
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.MONTH, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        long toyear = calendar.getTime().getTime() / 1000;
        if (time > toyear) {
            Calendar intime = Calendar.getInstance();
            intime.setTime(new Date(time * 1000L));
            return (intime.get(Calendar.MONTH) + 1) + "月" + intime.get(Calendar.DAY_OF_MONTH) + "日";
        }
        // 今年前
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        return simpleDateFormat.format(new Date(time * 1000));
    }

    /**
     * 获取年龄
     * @param birthday java.sql.Date 类的对象
     * @return 一个整数表示年龄
     */
    public static Integer getAge(java.sql.Date birthday) {
        return new Date().getYear() - birthday.getYear();
    }
}
