package ur.tools.cron.client.impl;

/**

 * Related Values Processing Framework.
 * 
 * Copyright (C) 2003 Serge Brisson.
 * 
 * This software is distributable under LGPL license.
 * See details at the bottom of this file.
 * 
 * $Header: /cvsroot/rvpf/RVPF/java/src/net/sf/rvpf/clock/Clock.java,v 1.2 2003/09/12 19:22:44 sfb Exp $
 */

import java.sql.Timestamp;
import java.util.Date;

/**
 * Clock.
 * 
 * 
 * 
 * @author Serge Brisson
 * 
 * @version $Revision: 1.2 $
 */

public class Clock {

    public static Clock getInstance(final long timestamp) {

        final Clock instance = new Clock();

        instance.date = new Date( timestamp );

        return instance;
    }

    Date date = null;


    private Clock() {

    }


    // Instance Properties.

    /**
     * Gets the day.
     * 
     * 
     * 
     * @return The day (1 - 31).
     */

    public int getDay() {

        return this.getDate().getDate();

    }


    /**
     * Sets the day.
     * 
     * 
     * 
     * @param day
     *            The day (1 - 31).
     * @throws CronException
     */

    public void setDay(final int day, final boolean reverse) {

        final Date date = this.getDate();
        date.setDate( day );
        this.setDate( date );

        if (reverse) {
            this.setHour( 23, reverse );
        }
        else {
            this.setHour( 0, reverse );
        }
    }


    /**
     * Gets the number of days in the month.
     * 
     * 
     * 
     * @return The number of days (28 - 31).
     */

    public int getDaysInMonth() {

        final int month = this.getDate().getMonth() + 1;

        final int year = this.getDate().getYear() + 1900;

        switch (month) {

            case 1:
                return 31;

            case 2:
                if ((year % 4 == 0) && ((year % 100 != 0) || (year % 400 == 0))) {
                    return 29;
                }
                return 28;

            case 3:
                return 31;

            case 4:
                return 30;

            case 5:
                return 31;

            case 6:
                return 30;

            case 7:
                return 31;

            case 8:
                return 31;

            case 9:
                return 30;

            case 10:
                return 31;

            case 11:
                return 30;

            case 12:
                return 31;

        }

        return -1;

    }


    /**
     * Gets the day of week.
     * 
     * 
     * 
     * @return The day of week (0 - 6 where 0 is Sunday).
     */

    public int getDayOfWeek() {

        return this.getDate().getDay();

    }


    private Date getDate() {

        return this.date;
    }


    private void setDate(final Date date) {

        this.date = date;
    }


    /**
     * Gets the hour.
     * 
     * 
     * 
     * @return The hour (0 - 23).
     */

    public int getHour() {

        return this.getDate().getHours();

    }


    public void setHour(final int hour, final boolean reverse) {

        final Date date = this.getDate();
        date.setHours( hour );

        this.setDate( date );

        if (reverse) {
            this.setMinute( 59 );
        }
        else {
            this.setMinute( 0 );
        }

    }


    /**
     * Gets the number of milliseconds since 1970-01-01 00:00:00.
     * 
     * 
     * 
     * @return The number of milliseconds.
     */

    public long getMillis() {

        return this.date.getTime();

    }


    /**
     * Gets the minute.
     * 
     * 
     * 
     * @return The minute (0 - 59).
     */

    public int getMinute() {

        return this.getDate().getMinutes();

    }


    /**
     * Sets the minute.
     * 
     * 
     * 
     * @param minute
     *            The minute (0 - 59).
     * @throws CronException
     */

    public void setMinute(final int minute) {

        final Date date = this.getDate();
        date.setMinutes( minute );
        this.setDate( date );

    }


    /**
     * Gets the month.
     * 
     * 
     * 
     * @return The month (1 - 12).
     */

    public int getMonth() {

        return this.getDate().getMonth() + 1;

    }


    /**
     * Sets the month.
     * 
     * 
     * 
     * @param month
     *            The month (1 - 12).
     * @throws CronException
     */

    public void setMonth(final int month, final boolean reverse) {

        if (!reverse) {
            this.setDay( 1, reverse );
        }
        final Date date = this.getDate();
        date.setMonth( month - 1 );
        this.setDate( date );

        if (reverse) {
            this.setDay( this.getDaysInMonth(), reverse );
        }

    }


    // Instance Methods.

    /**
     * Bumps the year.
     * 
     * @throws CronException
     */

    public void bumpYear(final boolean reverse) {

        int year = this.getDate().getYear();

        if (reverse) {
            year -= 1;
        }
        else {
            year += 1;
        }

        final Date date = this.getDate();
        date.setYear( year );
        this.setDate( date );

        if (reverse) {
            this.setMonth( 12, reverse );
        }
        else {
            this.setMonth( 1, reverse );
        }

    }


    /**
     * Clears the seconds.
     */

    public void clearSeconds() {

        this.date.setSeconds( 0 );
        final long ts = this.date.getTime() / 1000 * 1000;
        this.date = new Date( ts );

    }


    /**
     * Provides a String representation of the Clock's time.
     * 
     * 
     * 
     * @return The Clock's time in Timestamp format.
     */

    @Override
    public String toString() {

        return new Timestamp( this.getMillis() ).toString();

    }

    // Instance Attributes.

}

// $Log: Clock.java,v $

// Revision 1.2 2003/09/12 19:22:44 sfb

// Added clearSeconds.

//

/*
 * 
 * This is free software; you can redistribute it and/or modify
 * 
 * it under the terms of the GNU Lesser General Public License
 * 
 * as published by the Free Software Foundation; either version 2.1
 * 
 * of the License, or (at your option) any later version.
 * 
 * 
 * 
 * This software is distributed in the hope that it will be useful,
 * 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * 
 * Lesser General Public License for more details.
 * 
 * 
 * 
 * You should have received a copy of the GNU Lesser General Public
 * 
 * License along with this software; if not, write to the Free Software
 * 
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */

