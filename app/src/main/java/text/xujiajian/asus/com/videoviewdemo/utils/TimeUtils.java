package text.xujiajian.asus.com.videoviewdemo.utils;

import java.text.SimpleDateFormat;

/**
 * Created by asus on 2017/2/5.
 */

public class TimeUtils {
    public static String formatTime(int time) {
        if (time < 0) {
            return "00:00";
        }
        String pattern = "HH:mm:ss";

        if (time < 1000 * 60 * 60) {
            pattern = "mm:ss";
        }
        //格式化时间 毫秒自动转化时分秒
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String format = simpleDateFormat.format(time);
        return format;

    }
}
