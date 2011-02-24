package ur.tools.cron.client.impl;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import ur.tools.cron.client.CronIterator;
import ur.tools.cron.client.impl.Parser.BadItemException;

import com.google.gwt.i18n.client.TimeZone;

public class CronIteratorImpl implements CronIterator {

    private long endTimestamp = -1;
    private Crontab crontab;
    private final String cronString;
    private final TimeZone timezone;
    List<DaylightSavingTime> daylightSavingTimes;
    private long currentTimestamp;
    private int nextState = 0;
    private final static int DRIFT_RATE = 60000;
    Logger logger = Logger.getLogger( "CronLogger" );

    static Comparator<DaylightSavingTime> dstComparator =
            new Comparator<DaylightSavingTime>() {

                @Override
                public int compare(
                        final DaylightSavingTime dst1,
                        final DaylightSavingTime dst2) {

                    return (int) (dst1.getEndTimestamp() - dst2
                            .getStartTimestamp());
                }
            };


    public CronIteratorImpl(
            final String cronString,
            final long startTimestamp,
            final long endTimestamp,
            final TimeZone timezone,
            final List<DaylightSavingTime> daylightSavingTimes)
            throws CronException {

        this( cronString, startTimestamp, timezone, daylightSavingTimes );
        this.endTimestamp = this.getTimezoneCorrectedTimestamp( endTimestamp );

    }


    public CronIteratorImpl(
            final String cronString,
            final long startTimestamp,
            final TimeZone timezone,
            final List<DaylightSavingTime> daylightSavingTimes)
            throws CronException {

        this.cronString = cronString;
        this.timezone = timezone;
        this.daylightSavingTimes = daylightSavingTimes;
        this.setCronString( cronString );
        if (daylightSavingTimes != null) {
            Collections.sort( daylightSavingTimes, dstComparator );
        }
        this.currentTimestamp =
                this.getTimezoneCorrectedTimestamp( startTimestamp );
    }


    public boolean equals(final CronIterator cronIterator) {

        return this.cronString.equals( cronIterator.getCronString() );
    }


    @Override
    public CronIterator clone() {

        long startTimestamp = this.currentTimestamp;
        if (this.nextState < 0) {
            startTimestamp += DRIFT_RATE;
        }
        if (this.nextState > 0) {
            startTimestamp -= DRIFT_RATE;
        }
        try {
            return new CronIteratorImpl(
                    this.cronString,
                    this.getGMTTimestamp( startTimestamp ),
                    this.getGMTTimestamp( this.endTimestamp ),
                    this.timezone,
                    new LinkedList<DaylightSavingTime>(
                            this.daylightSavingTimes ) );
        }
        catch (final CronException e) {
            throw new RuntimeException( e );
        }
    }


    private void setCronString(final String cronString) throws CronException {

        try {
            this.crontab = Parser.parse( cronString );
        }
        catch (final BadItemException e) {

            throw new CronException( "Error while parsing cron string.", e );
        }

    }


    public boolean hasNext() {

        if (this.endTimestamp > 0) {

            final Clock clockCopy;

            if (this.nextState < 0) {
                clockCopy =
                        Clock.getInstance( this.currentTimestamp + 2
                                * DRIFT_RATE );
            }
            else {
                clockCopy = Clock.getInstance( this.currentTimestamp );
            }

            this.crontab.settle( clockCopy, false );

            if (this.endTimestamp > 0
                    && clockCopy.getMillis() > this.endTimestamp) {

                return false;
            }

        }

        return true;

    }


    public Long next() {

        // final Date now = new Date();
        /*
         * Die neue Uhrzeit wird eine Minute weitergedreht. Cron ist ja nur
         * minutengenau.
         */
        Clock clock = null;
        if (this.nextState < 0) {
            clock = Clock.getInstance( this.currentTimestamp + 2 * DRIFT_RATE );
        }
        else {
            clock = Clock.getInstance( this.currentTimestamp );
        }

        this.crontab.settle( clock, false );

        this.currentTimestamp = clock.getMillis() + DRIFT_RATE;
        this.nextState = 1;

        // this.logger.log( Level.INFO, "diff: "
        // + (new Date().getTime() - now.getTime()) );

        return this.getGMTTimestamp( this.currentTimestamp - DRIFT_RATE );

    }


    public void remove() {

        throw new RuntimeException( "Not implemented" );
    }


    @Override
    public String getCronString() {

        return this.cronString;
    }


    @Override
    public Long previous() {

        /*
         * Die neue Uhrzeit wird eine Minute weitergedreht. Cron ist ja nur
         * minutengenau.
         */
        Clock clock = null;
        this.currentTimestamp = this.currentTimestamp / 60000 * 60000;
        if (this.nextState > 0) {
            clock = Clock.getInstance( this.currentTimestamp - 2 * DRIFT_RATE );
        }
        else {

            clock = Clock.getInstance( this.currentTimestamp - DRIFT_RATE );
        }

        this.crontab.settle( clock, true );

        this.currentTimestamp = clock.getMillis() - DRIFT_RATE;
        this.nextState = -1;
        return this.getGMTTimestamp( this.currentTimestamp + DRIFT_RATE );
    }


    @Override
    public Long current() {

        return this.currentTimestamp + -1 * this.nextState * DRIFT_RATE;
    }


    private TimeZone getCorrectTimezone(final long timestamp) {

        if (this.daylightSavingTimes != null) {

            for (final DaylightSavingTime dst : this.daylightSavingTimes) {

                if (dst.getStartTimestamp() <= timestamp
                        && dst.getEndTimestamp() > timestamp) {
                    return dst.getTimezone();
                }

            }

        }

        return this.timezone;
    }


    private long getTimezoneCorrectedTimestamp(final long timestamp) {

        final TimeZone timezone = this.getCorrectTimezone( timestamp );
        return timestamp
                + (new Date( timestamp ).getTimezoneOffset() - timezone
                        .getStandardOffset()) * 60000l;

    }


    private long getGMTTimestamp(final long timestamp) {

        final TimeZone timezone = this.getCorrectTimezone( timestamp );
        return timestamp
                - (new Date( timestamp ).getTimezoneOffset() - timezone
                        .getStandardOffset()) * 60000l;
    }

}
