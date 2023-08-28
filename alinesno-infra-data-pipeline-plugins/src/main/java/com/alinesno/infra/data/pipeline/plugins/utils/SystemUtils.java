package com.alinesno.infra.data.pipeline.plugins.utils;

import java.io.File;

public class SystemUtils {

    public static String formatMilliseconds(long milliseconds) {
        long minutes = milliseconds / (1000 * 60);
        long seconds = (milliseconds / 1000) % 60;
        long remainingMilliseconds = milliseconds % 1000;

        StringBuilder formattedTime = new StringBuilder();

        if (minutes > 0) {
            formattedTime.append(minutes).append("分钟");
        }

        if (seconds > 0 || remainingMilliseconds > 0) {
            formattedTime.append(seconds).append("秒");
        }

        if (remainingMilliseconds > 0) {
            formattedTime.append(remainingMilliseconds).append("毫秒");
        }

        return formattedTime.toString();
    }
}
