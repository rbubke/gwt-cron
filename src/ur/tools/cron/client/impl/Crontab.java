package ur.tools.cron.client.impl;


/**

 * Related Values Processing Framework.

 * 

 * Copyright (C) 2003 Serge Brisson.

 * 

 * This software is distributable under LGPL license.

 * See details at the bottom of this file.

 * 

 * $Header: /cvsroot/rvpf/RVPF/java/src/net/sf/rvpf/clock/crontab/Crontab.java,v 1.3 2003/09/12 19:15:32 sfb Exp $

 */

/**
 * Crontab.
 * 
 * 
 * 
 * @author Serge Brisson
 * 
 * @version $Revision: 1.3 $
 */

public class Crontab {

    // Constructors.

    /**
     * Constructs a Crontab with the boolean arrays preset.
     * 
     * 
     * 
     * @param minutes
     *            Minutes in which to trigger events.
     * 
     * @param hours
     *            Hours in day in which to trigger events.
     * 
     * @param days
     *            Days in month in which to trigger events.
     * 
     * @param months
     *            Months in which to trigger events.
     * 
     * @param dows
     *            Days of week in which to trigger events.
     */

    public Crontab(

    final boolean[] minutes,

    final boolean[] hours,

    final boolean[] days,

    final boolean[] months,

    final boolean[] dows) {

        this.minutes = minutes;

        this.hours = hours;

        this.days = days;

        this.months = months;

        this.dows = dows;

    }


    // Instance Methods.

    /**
     * Advances the Clock to the next specified time.
     * 
     * 
     * 
     * @param clock
     *            The Clock.
     * @throws CronException
     */

    void advance(final Clock clock, final boolean reverse) throws CronException {

        if (!this.settle( clock, reverse )) {
            this.advanceMinute( clock, reverse );
        }

    }


    /**
     * Settles the Clock to a valid schedule time.
     * 
     * 
     * 
     * @param clock
     *            The Clock.
     * 
     * @return True if the Clock time has been modified.
     * @throws CronException
     */

    boolean settle(final Clock clock, final boolean reverse) {

        boolean settled = false;

        clock.clearSeconds();

        settled |= this.settleMonth( clock, reverse );

        settled |= this.settleDay( clock, reverse );

        settled |= this.settleHour( clock, reverse );

        settled |= this.settleMinute( clock, reverse );

        return settled;

    }


    // Private Instance Methods.

    private void advanceDay(final Clock clock, final boolean reverse) {

        int day = clock.getDay() - 1;

        final int days = clock.getDaysInMonth();

        int dow = clock.getDayOfWeek();

        assert days <= this.days.length;

        while ((!reverse && ++day < days) || (reverse && --day >= 0)) {

            if (!reverse && ++dow >= this.dows.length) {
                dow = 0;
            }

            if (reverse && --dow < 0) {
                dow = 6;
            }

            if (this.days[day] || this.dows[dow]) {

                clock.setDay( day + 1, reverse );

                return;

            }

        }

        this.advanceMonth( clock, reverse );

        this.settleDay( clock, reverse );

    }


    private void advanceHour(final Clock clock, final boolean reverse) {

        int hour = clock.getHour();

        while ((!reverse && ++hour < this.hours.length)
                || (reverse && --hour >= 0)) {
            if (this.hours[hour]) {

                clock.setHour( hour, reverse );

                return;

            }
        }

        this.advanceDay( clock, reverse );

        this.settleHour( clock, reverse );

    }


    private void advanceMinute(final Clock clock, final boolean reverse) {

        int minute = clock.getMinute();

        while ((!reverse && ++minute < this.minutes.length)
                || (reverse && --minute >= 0)) {
            if (this.minutes[minute]) {

                clock.setMinute( minute );

                return;

            }
        }

        this.advanceHour( clock, reverse );

        this.settleMinute( clock, reverse );

    }


    private void advanceMonth(final Clock clock, final boolean reverse) {

        int month = clock.getMonth() - 1;

        while ((!reverse && ++month < this.months.length)
                || (reverse && --month > 0)) {
            if (this.months[month]) {

                clock.setMonth( month + 1, reverse );

                return;

            }
        }

        clock.bumpYear( reverse );

        this.settleMonth( clock, reverse );

    }


    private boolean settleDay(final Clock clock, final boolean reverse) {

        if (this.days[clock.getDay() - 1]

        || this.dows[clock.getDayOfWeek()]) {
            return false;
        }

        this.advanceDay( clock, reverse );

        return true;

    }


    private boolean settleHour(final Clock clock, final boolean reverse) {

        if (this.hours[clock.getHour()]) {
            return false;
        }

        this.advanceHour( clock, reverse );

        return true;

    }


    private boolean settleMinute(final Clock clock, final boolean reverse) {

        if (this.minutes[clock.getMinute()]) {
            return false;
        }

        this.advanceMinute( clock, reverse );

        return true;

    }


    private boolean settleMonth(final Clock clock, final boolean reverse) {

        if (this.months[clock.getMonth() - 1]) {
            return false;
        }

        this.advanceMonth( clock, reverse );

        return true;

    }

    // Instance Attributes.

    private final boolean[] days;

    private final boolean[] dows;

    private final boolean[] hours;

    private final boolean[] minutes;

    private final boolean[] months;

}

// $Log: Crontab.java,v $

// Revision 1.3 2003/09/12 19:15:32 sfb

// Allowed package access to settle.

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

