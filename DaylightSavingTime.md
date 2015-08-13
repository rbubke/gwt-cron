# Introduction #

If you have to take care of defining daylight saving times you can do that with gwt-cron. Simple create an array of all necessary dst's in the future. The dst differs from normal timezone and therefore you define a dst with a starttime and endtime and a timezone valid for this interval.


# Details #

```
// every Sunday 10 o clock
final String cronString = "0 10 * * 0";

// start time is now
final long startTimestamp = new Date().getTime();

// timezone is CET
final TimeZone timezone = TimeZone.createTimeZone( -60 );

// DateTimeFormater for dst's
final DateTimeFormat dstFormat =
        DateTimeFormat.getFormat( "yyyy-MM-dd HH:mm ZZZZ" );

// daylight saving timezone for the next 2 years is MESZ
final List<DaylightSavingTime> dstList =
        new LinkedList<DaylightSavingTime>();

dstList.add( new DaylightSavingTime(
        TimeZone.createTimeZone( -120 ),
        dstFormat.parse( "2011-03-27 03:00 +0200" ).getTime(),
        dstFormat.parse( "2011-10-30 03:00 +0200" ).getTime() ) );

dstList.add( new DaylightSavingTime(
        TimeZone.createTimeZone( -120 ),
        dstFormat.parse( "2012-03-25 03:00 +0200" ).getTime(),
        dstFormat.parse( "2012-10-28 03:00 +0200" ).getTime() ) );


try {
    final CronIterator cronIterator =
            CronFactory.Util.getInstance().createCronIterator(
                    cronString,
                    startTimestamp,
                    timezone,
                    dstList );

    // here iterate over possible cron timestamps
    if (cronIterator.hasNext()) {

        final Date date = new Date( cronIterator.next() );
        GWT.log( "next date: " + date.toString() );
    }

}
catch (final CronException e) {
    e.printStackTrace();
}
```