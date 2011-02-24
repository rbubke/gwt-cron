package ur.tools.cron.client;

import java.util.Iterator;

public interface CronIterator extends Iterator<Long> {

    public String getCronString();


    public Long previous();


    public Long current();


    public CronIterator clone();
}
