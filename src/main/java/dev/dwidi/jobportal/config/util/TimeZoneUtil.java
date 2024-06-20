package dev.dwidi.jobportal.config.util;

import dev.dwidi.jobportal.exception.InvalidTimeZoneException;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.zone.ZoneRulesException;

@Component
public class TimeZoneUtil {

    public String convertToLocalTime(ZonedDateTime utcTime, String clientTimeZone) {
        return utcTime.withZoneSameInstant(ZoneId.of(clientTimeZone)).toString();
    }

    public void validateTimeZone(String timeZone) {
        try {
            ZoneId.of(timeZone);
        } catch (ZoneRulesException e) {
            throw new InvalidTimeZoneException("Invalid time zone: " + timeZone + ". Timezone must be in the format: Asia/Jakarta");
        }
    }
}