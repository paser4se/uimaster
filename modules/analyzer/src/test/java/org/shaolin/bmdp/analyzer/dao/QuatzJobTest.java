package org.shaolin.bmdp.analyzer.dao;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.shaolin.bmdp.analyzer.be.IJavaCCJob;
import org.shaolin.bmdp.analyzer.distributed.DispatcherJob;
import org.shaolin.bmdp.analyzer.distributed.api.IJobDispatcher;

/**
 * Created by lizhiwe on 4/18/2016.
 */
public class QuatzJobTest {
	private Logger logger = Logger.getLogger(getClass());
	private final SchedulerFactory scheduleFactory = new StdSchedulerFactory();
	private Scheduler scheduler;

	@Test
	public void testScheduler() throws Exception {
	    
	    IJobDispatcher dispatcher = new IJobDispatcher() {

	    	@Override
	    	public void configService() {
	    		
	    	}
	    	
            @Override
            public int getRunLevel() {
                // TODO Auto-generated method stub
                return 0;
            }

            @Override
            public boolean readyToStop() {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public void reload() {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void startService() {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void stopService() {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void loadAllJobs() {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void scheduleJobs() {
                // TODO Auto-generated method stub
                
            }

            
            private int c = 0;
            @Override
            public void dispatchJobs(IJavaCCJob jobInfo) {
               System.out.println("----------running:"+c++);
                
            }

            @Override
            public String getCronExpressionFromJobInfo(IJavaCCJob jobInfo) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public String getJobNameFromJobInfo(IJavaCCJob jobInfo) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public String getTriggerNameFromJobInfo(IJavaCCJob jobInfo) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public void startScheduleJob(IJavaCCJob job) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void cancelScheduleJob(IJavaCCJob job) {
                // TODO Auto-generated method stub
                
            }
	        
	    };
	    
		scheduler = scheduleFactory.getScheduler();

		final JobDetail jobDetail = JobBuilder.newJob(DispatcherJob.class)
				.withIdentity("job1", "group1").build();

		final CronTrigger trigger = TriggerBuilder
				.newTrigger()
				.withIdentity("scheduler1", "group1")
				.withSchedule(
						CronScheduleBuilder.cronSchedule("* 0/3 * * * ?"))
				.build();

		trigger.getJobDataMap().put(DispatcherJob.JOB_DISPATCHER, dispatcher);
		
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					scheduler.scheduleJob(jobDetail, trigger);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		t.start();

		scheduler.start();
		logger.info("-----------start running schedule");
		Thread.sleep(4000);
		logger.info("-----------clear schedule");
		scheduler.clear();

		Thread.sleep(4000);
		logger.info("-----------start schedule again");
		scheduler.start();
		scheduler.scheduleJob(jobDetail, trigger);

		Thread.sleep(4000);

	}
}
