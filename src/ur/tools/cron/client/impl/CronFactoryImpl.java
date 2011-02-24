package ur.tools.cron.client.impl;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import ur.tools.cron.client.CronFactory;
import ur.tools.cron.client.CronIterator;
import ur.tools.cron.client.impl.CronBatchIterator.CronBatchIteratorRequestor;

import com.google.gwt.i18n.client.TimeZone;

public class CronFactoryImpl implements CronFactory {

    private static CronFactoryImpl instance;


    private CronFactoryImpl() {

    }


    public static CronFactory getInstance() {

        if (CronFactoryImpl.instance == null) {
            CronFactoryImpl.instance = new CronFactoryImpl();
        }
        return CronFactoryImpl.instance;
    }


    public CronIterator createCronIterator(
            final String cronString,
            final long startTimestamp,
            final TimeZone timezone,
            final List<DaylightSavingTime> daylightSavingTimes)
            throws CronException {

        return new CronIteratorImpl(
                cronString,
                startTimestamp,
                timezone,
                daylightSavingTimes );
    }


    public CronIterator createCronIterator(
            final String[] cronStringArray,
            final long startTimestamp,
            final TimeZone timezone,
            final List<DaylightSavingTime> daylightSavingTimes)
            throws CronException {

        final Collection<CronIterator> cronIteratorCollection =
                new HashSet<CronIterator>();

        for (final String cronString : cronStringArray) {

            cronIteratorCollection.add( this.createCronIterator(
                    cronString,
                    startTimestamp,
                    timezone,
                    daylightSavingTimes ) );
        }

        return new CronArrayIterator( cronIteratorCollection );

    }


    public CronIterator createCronIterator(
            final String cronString,
            final long startTimestamp,
            final long endTimestamp,
            final TimeZone timezone,
            final List<DaylightSavingTime> daylightSavingTimes)
            throws CronException {

        return new CronIteratorImpl(
                cronString,
                startTimestamp,
                endTimestamp,
                timezone,
                daylightSavingTimes );
    }


    public Iterator<CronIterator> createCronBatchIterator(
            final String cronString,
            final long startTimestamp,
            final int batchIntervalInMinutes,
            final TimeZone timezone,
            final List<DaylightSavingTime> daylightSavingTimes) {

        return new CronBatchIterator(
                cronString,
                startTimestamp,
                batchIntervalInMinutes,
                timezone,
                daylightSavingTimes,
                new CronBatchIteratorRequestor() {

                    public CronIterator createCronIterator(
                            final String cronString,
                            final long startTimestamp,
                            final long endTimestamp,
                            final TimeZone timezone,
                            final List<DaylightSavingTime> daylightSavingTimes) {

                        try {
                            return CronFactoryImpl.this.createCronIterator(
                                    cronString,
                                    startTimestamp,
                                    endTimestamp,
                                    timezone,
                                    daylightSavingTimes );
                        }
                        catch (final CronException e) {
                            throw new RuntimeException();
                        }
                    }
                } );
    }


    public Iterator<CronIterator> createCronBatchIterator(
            final String cronString,
            final long startTimestamp,
            final long endTimestamp,
            final int batchIntervalInMinutes,
            final TimeZone timezone,
            final List<DaylightSavingTime> daylightSavingTimes) {

        return new CronBatchIterator(
                cronString,
                startTimestamp,
                endTimestamp,
                batchIntervalInMinutes,
                timezone,
                daylightSavingTimes,
                new CronBatchIteratorRequestor() {

                    public CronIterator createCronIterator(
                            final String cronString,
                            final long startTimestamp,
                            final long endTimestamp,
                            final TimeZone timezone,
                            final List<DaylightSavingTime> daylightSavingTimes) {

                        try {
                            return CronFactoryImpl.this.createCronIterator(
                                    cronString,
                                    startTimestamp,
                                    endTimestamp,
                                    timezone,
                                    daylightSavingTimes );
                        }
                        catch (final CronException e) {
                            throw new RuntimeException();
                        }
                    }
                } );
    }

}
