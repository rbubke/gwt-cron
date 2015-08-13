# Introduction #

This cron interpreter reads a cron string and returns an iterator which outputs timestamps.
`CronFactory.Util.getInstance()` creates an instance of the cron factory which can create cron iterator's.
If you have to take care of daylight saving times [look at these example](http://code.google.com/p/gwt-cron/wiki/DaylightSavingTime).

# Details #
Extend your module xml file by inheriting gwtcron:
```
<inherits name="ur.tools.cron.GwtCron"/>
```

Use gwt-cron to generate timestamps
```
import java.util.Date;
import ur.tools.cron.client.impl.CronException;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.TimeZone;

....


// every Sunday 10 o clock
final String cronString = "0 10 * * 0";

// start time is now
final long startTimestamp = new Date().getTime();

// timezone is CET
final TimeZone timezone = TimeZone.createTimeZone( -60 );

try {
    final CronIterator cronIterator =
            CronFactory.Util.getInstance().createCronIterator(
                    cronString,
                    startTimestamp,
                    timezone,
                    null );

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