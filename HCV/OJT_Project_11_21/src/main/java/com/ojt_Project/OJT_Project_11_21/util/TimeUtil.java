package com.ojt_Project.OJT_Project_11_21.util;

import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;

@Component
public class TimeUtil {
    public static String formatDuration(Duration duration){
        long minutes = duration.toMinutes();
        long seconds = duration.getSeconds() % 60;
        return String.format("%02d:%02d",minutes,seconds);
    }

    public static String getRemainingTime(LocalDateTime endDate) {
        Duration duration = Duration.between(LocalDateTime.now(), endDate);
        if (duration.isNegative() || duration.isZero()) {
            return "00:00"; // Thời gian đã hết
        }
        return formatDuration(duration);
    }
}
