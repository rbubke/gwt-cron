package ur.tools.cron.client.impl;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import ur.tools.cron.client.CronIterator;

public class CronArrayIterator implements CronIterator {

    Collection<CronIterator> cronIteratorList;
    Long currentTimestamp;
    String currentCronString;
    CronIterator lastCronIterator;


    public CronArrayIterator(final Collection<CronIterator> cronIteratorList) {

        this.cronIteratorList = cronIteratorList;

        // init current timestamp
        final Iterator<CronIterator> cronIteratorIt =
                cronIteratorList.iterator();
        this.currentTimestamp = Long.MAX_VALUE;
        if (cronIteratorIt.hasNext()) {
            final Long current = cronIteratorIt.next().current();
            if (current < this.currentTimestamp) {
                this.currentTimestamp = current;
            }

        }
    }


    @Override
    public CronIterator clone() {

        final Collection<CronIterator> clonedCronIteratorList =
                new HashSet<CronIterator>();
        CronIterator clonedLastCronIterator = null;
        for (final CronIterator cronIterator : this.cronIteratorList) {
            if (cronIterator == lastCronIterator) {
                clonedLastCronIterator = cronIterator.clone();
                clonedCronIteratorList.add( clonedLastCronIterator );
            }
            else {
                clonedCronIteratorList.add( cronIterator.clone() );
            }
        }
        final CronArrayIterator cronArrayIterator =
                new CronArrayIterator( clonedCronIteratorList );
        cronArrayIterator.currentCronString = this.currentCronString;
        cronArrayIterator.currentTimestamp = this.currentTimestamp;
        cronArrayIterator.lastCronIterator = clonedLastCronIterator;
        return cronArrayIterator;
    }


    @Override
    public boolean hasNext() {

        for (final CronIterator cronIterator : this.cronIteratorList) {
            if (this.getNextTimestamp( cronIterator ) != null) {
                return true;
            }
        }

        return false;
    }


    @Override
    public Long next() {

        Long nearestTimestamp = Long.MAX_VALUE;
        CronIterator nearestCronIterator = null;
        for (final CronIterator cronIterator : this.cronIteratorList) {

            final Long currentTimestamp = this.getNextTimestamp( cronIterator );
            if (currentTimestamp != null) {

                if (currentTimestamp < nearestTimestamp) {

                    nearestCronIterator = cronIterator;
                    nearestTimestamp = currentTimestamp;
                }
            }
        }

        if (nearestCronIterator == null) {
            throw new RuntimeException( "There is no next timestamp available." );
        }

        this.lastCronIterator = nearestCronIterator;
        this.currentTimestamp = nearestCronIterator.current();
        this.currentCronString = nearestCronIterator.getCronString();
        return this.currentTimestamp;
    }


    @Override
    public Long previous() {

        Long nearestTimestamp = Long.MIN_VALUE;
        CronIterator nearestCronIterator = null;
        for (final CronIterator cronIterator : this.cronIteratorList) {

            final Long currentTimestamp =
                    this.getPreviousTimestamp( cronIterator );
            if (currentTimestamp != null) {

                if (currentTimestamp > nearestTimestamp) {

                    nearestCronIterator = cronIterator;
                    nearestTimestamp = currentTimestamp;
                }
            }
        }

        if (nearestCronIterator == null) {
            throw new RuntimeException( "There is no next timestamp available." );
        }

        this.lastCronIterator = nearestCronIterator;
        this.currentTimestamp = nearestCronIterator.current();
        this.currentCronString = nearestCronIterator.getCronString();
        return this.currentTimestamp;
    }


    private Long getNextTimestamp(final CronIterator cronIt) {

        while (cronIt.previous() >= this.currentTimestamp) {
        }

        if (cronIt == this.lastCronIterator) {
            // wenn mehrere Iteratoren den gleichen wert erzeugen, sollen diese
            // werte natuerlich als naechstes zurueckgegeben werden. d.h. der
            // timestamp von next() kann mehrmals hintereinander der gleiche
            // sein, wenn mehrere iteratoren den gleichen zeitstempel erzeugen.
            while (cronIt.hasNext() && cronIt.next() <= this.currentTimestamp) {
            }
        }
        else {
            while (cronIt.hasNext() && cronIt.next() < this.currentTimestamp) {
            }
        }

        return cronIt.current();

    }


    private Long getPreviousTimestamp(final CronIterator cronIt) {

        while (cronIt.hasNext() && cronIt.next() <= this.currentTimestamp) {
        }

        if (cronIt == this.lastCronIterator) {
            // wenn mehrere Iteratoren den gleichen wert erzeugen, sollen diese
            // werte natuerlich als naechstes zurueckgegeben werden. d.h. der
            // timestamp von next() kann mehrmals hintereinander der gleiche
            // sein, wenn mehrere iteratoren den gleichen zeitstempel erzeugen.
            while (cronIt.previous() >= this.currentTimestamp) {
            }
        }
        else {
            while (cronIt.previous() > this.currentTimestamp) {
            }
        }

        return cronIt.current();

    }


    @Override
    public void remove() {

        throw new RuntimeException( "Not implemented" );
    }


    @Override
    public String getCronString() {

        return this.currentCronString;
    }


    @Override
    public Long current() {

        return this.currentTimestamp;
    }

}
