package org.shaolin.bmdp.analyzer.dao;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.shaolin.bmdp.analyzer.distributed.DispatcherJob;

/**
 * Created by lizhiwe on 4/18/2016.
 */
public class QuatzJobTest {
    private Logger logger = Logger.getLogger(getClass());
    private final SchedulerFactory scheduleFactory = new StdSchedulerFactory();
    private Scheduler scheduler;

    @Test
    public void testScheduler() throws Exception {
        scheduler = scheduleFactory.getScheduler();

        final JobDetail jobDetail = JobBuilder.newJob(DispatcherJob.class).withIdentity("job1", "group1").build();

       final CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity("scheduler1", "group1")
                .withSchedule(CronScheduleBuilder.cronSchedule("0/20 * * * * ?")).build();



        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    scheduler.scheduleJob(jobDetail,trigger);
                }catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }) ;

        t.start();

        scheduler.start();
        logger.info("-----------start running schedule");
        Thread.sleep(40000);
        logger.info("-----------clear schedule");
        scheduler.clear();

        Thread.sleep(40000);
        logger.info("-----------start schedule again");
        scheduler.start();
        scheduler.scheduleJob(jobDetail,trigger);

        Thread.sleep(120000);


    }
}
