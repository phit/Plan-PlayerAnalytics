package com.djrapitops.plan.data.calculation;

import com.djrapitops.plan.data.PlayerProfile;
import com.djrapitops.plan.data.container.Session;
import com.djrapitops.plan.system.settings.Settings;
import com.djrapitops.plan.utilities.FormatUtils;
import com.djrapitops.plugin.api.TimeAmount;

import java.util.List;
import java.util.stream.Collectors;

public class ActivityIndex {

    private final double value;

    public ActivityIndex(PlayerProfile player, long date) {
        value = calculate(player, date);
    }

    public static String[] getGroups() {
        return new String[]{"Very Active", "Active", "Regular", "Irregular", "Inactive"};
    }

    private long loadSetting(long value) {
        return value <= 0 ? 1 : value;
    }

    private int loadSetting(int value) {
        return value <= 0 ? 1 : value;
    }

    private double calculate(PlayerProfile player, long date) {
        long week = TimeAmount.WEEK.ms();
        long weekAgo = date - week;
        long twoWeeksAgo = date - 2L * week;
        long threeWeeksAgo = date - 3L * week;

        long activePlayThreshold = loadSetting(Settings.ACTIVE_PLAY_THRESHOLD.getNumber() * TimeAmount.MINUTE.ms());
        int activeLoginThreshold = loadSetting(Settings.ACTIVE_LOGIN_THRESHOLD.getNumber());

        List<Session> sessionsWeek = player.getSessions(weekAgo, date).collect(Collectors.toList());
        List<Session> sessionsWeek2 = player.getSessions(twoWeeksAgo, weekAgo).collect(Collectors.toList());
        List<Session> sessionsWeek3 = player.getSessions(threeWeeksAgo, twoWeeksAgo).collect(Collectors.toList());

        // Playtime per week multipliers, max out to avoid too high values.
        double max = 4.0;

        long playtimeWeek = PlayerProfile.getActivePlaytime(sessionsWeek.stream());
        double weekPlay = (playtimeWeek * 1.0 / activePlayThreshold);
        if (weekPlay > max) {
            weekPlay = max;
        }
        long playtimeWeek2 = PlayerProfile.getActivePlaytime(sessionsWeek2.stream());
        double week2Play = (playtimeWeek2 * 1.0 / activePlayThreshold);
        if (week2Play > max) {
            week2Play = max;
        }
        long playtimeWeek3 = PlayerProfile.getActivePlaytime(sessionsWeek3.stream());
        double week3Play = (playtimeWeek3 * 1.0 / activePlayThreshold);
        if (week3Play > max) {
            week3Play = max;
        }

        double playtimeMultiplier = 1.0;
        if (playtimeWeek + playtimeWeek2 + playtimeWeek3 > activePlayThreshold * 3.0) {
            playtimeMultiplier = 1.25;
        }

        // Reduce the harshness for new players and players who have had a vacation
        if (weekPlay > 1 && week3Play > 1 && week2Play == 0.0) {
            week2Play = 0.5;
        }
        if (weekPlay > 1 && week2Play == 0.0) {
            week2Play = 0.6;
        }
        if (weekPlay > 1 && week3Play == 0.0) {
            week3Play = 0.75;
        }

        double playAvg = (weekPlay + week2Play + week3Play) / 3.0;

        double weekLogin = sessionsWeek.size() >= activeLoginThreshold ? 1.0 : 0.5;
        double week2Login = sessionsWeek2.size() >= activeLoginThreshold ? 1.0 : 0.5;
        double week3Login = sessionsWeek3.size() >= activeLoginThreshold ? 1.0 : 0.5;

        double loginMultiplier = 1.0;
        double loginTotal = weekLogin + week2Login + week3Login;
        double loginAvg = loginTotal / 3.0;

        if (loginTotal <= 2.0) {
            // Reduce index for players that have not logged in the threshold amount for 2 weeks
            loginMultiplier = 0.75;
        }

        return playAvg * loginAvg * loginMultiplier * playtimeMultiplier;
    }

    public double getValue() {
        return value;
    }

    public String getFormattedValue() {
        return FormatUtils.cutDecimals(value);
    }

    public String getGroup() {
        if (value >= 3.5) {
            return "Very Active";
        } else if (value >= 1.75) {
            return "Active";
        } else if (value >= 1.0) {
            return "Regular";
        } else if (value >= 0.5) {
            return "Irregular";
        } else {
            return "Inactive";
        }
    }

    public String getColor() {
        if (value >= 3.5) {
            return "green";
        } else if (value >= 1.75) {
            return "green";
        } else if (value >= 1.0) {
            return "lime";
        } else if (value >= 0.5) {
            return "amber";
        } else {
            return "blue-gray";
        }
    }
}
