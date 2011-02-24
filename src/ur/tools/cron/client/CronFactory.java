package ur.tools.cron.client;

import java.util.Iterator;
import java.util.List;

import ur.tools.cron.client.impl.CronException;
import ur.tools.cron.client.impl.CronFactoryImpl;
import ur.tools.cron.client.impl.DaylightSavingTime;

import com.google.gwt.i18n.client.TimeZone;

public interface CronFactory {

    public class Util {

        public static CronFactory getInstance() {

            return CronFactoryImpl.getInstance();
        }
    }


    public CronIterator createCronIterator(
            final String cronString,
            final long startTimestamp,
            TimeZone timezone,
            final List<DaylightSavingTime> daylightSavingTimes)
            throws CronException;


    public CronIterator createCronIterator(
            final String[] cronStringArray,
            final long startTimestamp,
            TimeZone timezone,
            final List<DaylightSavingTime> daylightSavingTimes)
            throws CronException;


    public CronIterator createCronIterator(
            final String cronString,
            final long startTimestamp,
            final long endTimestamp,
            TimeZone timezone,
            final List<DaylightSavingTime> daylightSavingTimes)
            throws CronException;


    public Iterator<CronIterator> createCronBatchIterator(
            String cronString,
            final long startTimestamp,
            int batchIntervalInMinutes,
            TimeZone timezone,
            final List<DaylightSavingTime> daylightSavingTimes);


    public Iterator<CronIterator> createCronBatchIterator(
            String cronString,
            final long startTimestamp,
            final long endTimestamp,
            int batchIntervalInMinutes,
            TimeZone timezone,
            final List<DaylightSavingTime> daylightSavingTimes);

}
