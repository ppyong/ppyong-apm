package com.devluff.agent.scheduler;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AgentScheduleManager {
	
	private static final Logger logger = LoggerFactory.getLogger(AgentScheduleManager.class);
	
	private Scheduler oSchduler;
	private AgentScheduleInfo oAgentScheduleInfo;
	
	public void setScheduleConfig(AgentScheduleInfo oInfo) {
		this.oAgentScheduleInfo = oInfo;
	}
	
	/**
	 * 정책에 따른 스케쥴을 실행한다. 
	 * @return
	 */
	public boolean startSchedule() {
		CronTrigger oCronTrigger  = TriggerBuilder.newTrigger()
				.withIdentity("trigger1", "triggerGroup1")
		        .withSchedule(CronScheduleBuilder.cronSchedule(oAgentScheduleInfo.getCronExp()))
		        .build();
		
		JobDetail jobDetail = JobBuilder.newJob(AgentResourceCheckJob.class)
                .withIdentity("job1", "jobGroup1").build();
		
		SchedulerFactory scheduleFactory = new StdSchedulerFactory();
		try {
			oSchduler = scheduleFactory.getScheduler();
			oSchduler.scheduleJob(jobDetail, oCronTrigger);
			oSchduler.start();
		}catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		}
		return true;
	}
	
	/**
	 * 변경된 정책에 따라 스케쥴을 변경하여 실행한다. 
	 * @return
	 */
	public boolean reloadSchedule() {
		try {
			CronTrigger oOldCronTrigger = (CronTrigger) oSchduler.getTrigger(new TriggerKey("trigger1", "triggerGroup1"));
			TriggerBuilder<CronTrigger> oTriggerBuilder = oOldCronTrigger.getTriggerBuilder();
			CronTrigger oNewCronTrigger = oTriggerBuilder.withSchedule(CronScheduleBuilder.cronSchedule(oAgentScheduleInfo.getCronExp())).build();
			oSchduler.rescheduleJob(new TriggerKey("trigger1", "triggerGroup1"), oNewCronTrigger);
		}catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		}
	
		return true;
	}
	
}
