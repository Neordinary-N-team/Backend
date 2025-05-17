package neordinary.backend.nteam.utils;

import java.time.DayOfWeek;
import java.time.LocalDate;

public class WeekDateCalculator {
    public static LocalDate calculateDateOfDayOfWeek(String dayOfWeek) {
        LocalDate today = LocalDate.now(); // 오늘 날짜
        DayOfWeek targetDay = DayOfWeek.valueOf(dayOfWeek); // 목표 요일 (화요일)

        // 오늘 날짜 기준으로 목표 요일까지의 차이 계산
        int daysUntilTarget = targetDay.getValue() - today.getDayOfWeek().getValue();

        // 만약 차이가 음수라면, 다음 주의 해당 요일로 계산
        if (daysUntilTarget < 0) {
            daysUntilTarget += 7;
        }

        return today.plusDays(daysUntilTarget); // 목표 날짜 계산
    }
}
