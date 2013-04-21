package guardian;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * @author Sandeep Manchem
 *
 */
public class BuzzGuardianConfig implements ServletContextListener {

    private ScheduledExecutorService scheduler;
    public ArrayList<String> list = new ArrayList<String>();
    SMSProcessorService service = new SMSProcessorService(list);

    @Override
    public void contextInitialized(ServletContextEvent event) {
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(service, 0, 30, TimeUnit.SECONDS);
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        scheduler.shutdownNow();
    }
}

