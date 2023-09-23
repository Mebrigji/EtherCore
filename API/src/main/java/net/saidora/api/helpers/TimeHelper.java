package net.saidora.api.helpers;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TimeHelper {

    public static TimeHelper parse(String time, boolean future) {
        long t;
        try {
            Pattern timePattern = Pattern.compile("(?:([0-9]+)\\s*y[a-z]*[,\\s]*)?(?:([0-9]+)\\s*mo[a-z]*[,\\s]*)?(?:([0-9]+)\\s*w[a-z]*[,\\s]*)?(?:([0-9]+)\\s*d[a-z]*[,\\s]*)?(?:([0-9]+)\\s*h[a-z]*[,\\s]*)?(?:([0-9]+)\\s*m[a-z]*[,\\s]*)?(?:([0-9]+)\\s*(?:s[a-z]*)?)?", 2);
            Matcher m = timePattern.matcher(time);
            int years = 0;
            int months = 0;
            int weeks = 0;
            int days = 0;
            int hours = 0;
            int minutes = 0;
            int seconds = 0;
            boolean found = false;

            while (m.find()) {
                if (m.group() != null && !m.group().isEmpty()) {
                    for (int i = 0; i < m.groupCount(); ++i) {
                        if (m.group(i) != null && !m.group(i).isEmpty()) {
                            found = true;
                            break;
                        }
                    }

                    if (found) {
                        if (m.group(1) != null && !m.group(1).isEmpty()) {
                            years = Integer.parseInt(m.group(1));
                        }

                        if (m.group(2) != null && !m.group(2).isEmpty()) {
                            months = Integer.parseInt(m.group(2));
                        }

                        if (m.group(3) != null && !m.group(3).isEmpty()) {
                            weeks = Integer.parseInt(m.group(3));
                        }

                        if (m.group(4) != null && !m.group(4).isEmpty()) {
                            days = Integer.parseInt(m.group(4));
                        }

                        if (m.group(5) != null && !m.group(5).isEmpty()) {
                            hours = Integer.parseInt(m.group(5));
                        }

                        if (m.group(6) != null && !m.group(6).isEmpty()) {
                            minutes = Integer.parseInt(m.group(6));
                        }

                        if (m.group(7) != null && !m.group(7).isEmpty()) {
                            seconds = Integer.parseInt(m.group(7));
                        }
                        break;
                    }
                }
            }

            if (!found) {
                t = -1;
            } else {
                Calendar c = new GregorianCalendar();
                if (years > 0) {
                    c.add(Calendar.YEAR, years * (future ? 1 : -1));
                }

                if (months > 0) {
                    c.add(Calendar.MONTH, months * (future ? 1 : -1));
                }

                if (weeks > 0) {
                    c.add(Calendar.WEEK_OF_YEAR, weeks * (future ? 1 : -1));
                }

                if (days > 0) {
                    c.add(Calendar.DATE, days * (future ? 1 : -1));
                }

                if (hours > 0) {
                    c.add(Calendar.HOUR_OF_DAY, hours * (future ? 1 : -1));
                }

                if (minutes > 0) {
                    c.add(Calendar.MINUTE, minutes * (future ? 1 : -1));
                }

                if (seconds > 0) {
                    c.add(Calendar.SECOND, seconds * (future ? 1 : -1));
                }

                Calendar max = new GregorianCalendar();
                max.add(1, 10);
                t = c.after(max) ? max.getTimeInMillis() : c.getTimeInMillis();
            }

            return new TimeHelper(t);
        } catch (Exception var14) {
            return new TimeHelper(-1);
        }
    }

    private long millis;

    public TimeHelper(long millis){
        this.millis = millis;
    }

    public String parse(){
        String format = "";

        long days, hours, minutes, seconds;

        days = TimeUnit.MILLISECONDS.toDays(millis);
        millis -= TimeUnit.DAYS.toMillis(days);

        if(days > 0) format += days + "d ";

        hours = TimeUnit.MILLISECONDS.toHours(millis);
        millis -= TimeUnit.HOURS.toMillis(hours);

        if(hours > 0) format += hours + "h ";

        minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
        millis -= TimeUnit.MINUTES.toMillis(minutes);

        if(minutes > 0) format += minutes + "min ";

        seconds = TimeUnit.MILLISECONDS.toSeconds(millis);
        millis -= TimeUnit.SECONDS.toMillis(seconds);

        if(seconds > 0) format += seconds + "sek";

        return format.isEmpty() ? "<1sek" : format;
    }

    public String timestamp(){
        millis -= System.currentTimeMillis();
        millis *= -1;
        return parse();
    }

    public String remainingTime(){
        millis -= System.currentTimeMillis();
        return parse();
    }

    public long getMillis() {
        return millis;
    }

    private static final SimpleDateFormat defaultFormat = new SimpleDateFormat("hh:mm:ss dd/MM/yy");

    public String toString(){
        return defaultFormat.format(millis);
    }
}
