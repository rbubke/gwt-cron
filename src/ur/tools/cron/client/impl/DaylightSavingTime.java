package ur.tools.cron.client.impl;

import com.google.gwt.i18n.client.TimeZone;

public class DaylightSavingTime {

    TimeZone timezone;

    long startTimestamp;

    long endTimestamp;


    public DaylightSavingTime(
            final TimeZone timezone,
            final long startTimestamp,
            final long endTimestamp) {

        this.timezone = timezone;
        this.startTimestamp = startTimestamp;
        this.endTimestamp = endTimestamp;
    }


    public TimeZone getTimezone() {

        return this.timezone;
    }


    public void setTimezone(final TimeZone timezone) {

        this.timezone = timezone;
    }


    public long getStartTimestamp() {

        return this.startTimestamp;
    }


    public void setStartTimestamp(final long startTimestamp) {

        this.startTimestamp = startTimestamp;
    }


    public long getEndTimestamp() {

        return this.endTimestamp;
    }


    public void setEndTimestamp(final long endTimestamp) {

        this.endTimestamp = endTimestamp;
    }

}
