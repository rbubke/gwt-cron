package ur.tools.cron.client.impl;

import java.util.Iterator;
import java.util.List;

import ur.tools.cron.client.CronIterator;

import com.google.gwt.i18n.client.TimeZone;

public class CronBatchIterator implements Iterator<CronIterator> {

    public interface CronBatchIteratorRequestor {

        public CronIterator createCronIterator(
                final String cronString,
                final long startDate,
                final long endDate,
                TimeZone timezone,
                final List<DaylightSavingTime> daylightSavingTimes);
    }

    private final String cronString;
    private final long startTimestamp;
    private long endTimestamp = -1;
    private final int batchIntervalInMinutes;
    private final CronBatchIteratorRequestor requestor;
    private long currentBatch = 0;
    private final TimeZone timezone;
    final List<DaylightSavingTime> daylightSavingTimes;


    public CronBatchIterator(
            final String cronString,
            final long startTimestamp,
            final int batchIntervalInMinutes,
            final TimeZone timezone,
            final List<DaylightSavingTime> daylightSavingTimes,
            final CronBatchIteratorRequestor requestor) {

        this.cronString = cronString;
        this.startTimestamp = startTimestamp;
        this.batchIntervalInMinutes = batchIntervalInMinutes;
        this.requestor = requestor;
        this.timezone = timezone;
        this.daylightSavingTimes = daylightSavingTimes;
    }


    public CronBatchIterator(
            final String cronString,
            final long startTimestamp,
            final long endTimestamp,
            final int batchIntervalInMinutes,
            final TimeZone timezone,
            final List<DaylightSavingTime> daylightSavingTimes,
            final CronBatchIteratorRequestor requestor) {

        this(
                cronString,
                startTimestamp,
                batchIntervalInMinutes,
                timezone,
                daylightSavingTimes,
                requestor );
        this.endTimestamp = endTimestamp;
    }


    public boolean hasNext() {

        return this.endTimestamp < 0
                || this.endTimestamp > this.nextBatchStartDate();
    }


    public CronIterator next() {

        if (!this.hasNext()) {
            throw new RuntimeException(
                    "There is not further batch interval available. Please check with hasNext() for further batches." );
        }

        final CronIterator cronIterator =
                this.requestor.createCronIterator(
                        this.cronString,
                        this.nextBatchStartDate(),
                        this.nextBatchEndDate(),
                        this.timezone,
                        this.daylightSavingTimes );

        this.currentBatch++;

        return cronIterator;
    }


    private long nextBatchStartDate() {

        return this.startTimestamp + this.currentBatch
                * this.batchIntervalInMinutes * 1000L * 60L;
    }


    private long nextBatchEndDate() {

        final long endTimestamp =
                this.nextBatchStartDate() + this.batchIntervalInMinutes * 1000L
                        * 60L;

        if (this.endTimestamp > 0 && endTimestamp > this.endTimestamp) {
            return this.endTimestamp;
        }

        return endTimestamp;
    }


    public void remove() {

        // TODO Auto-generated method stub

    }

}
