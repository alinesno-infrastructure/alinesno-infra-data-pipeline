package com.alinesno.infra.data.pipeline.scheduler.listener;

import com.alinesno.infra.data.pipeline.enums.JobStatusEnums;
import com.alinesno.infra.data.pipeline.service.IJobInstanceService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;

/**
 * 跟触发器有关的事件包括：触发器被触发，触发器触发失败，以及触发器触发完成（触发器完成后作业任务开始运行）。
 *
 * @author luoxiaodong
 * @version 1.0.0
 */
@Slf4j
public class PipelineJobListener implements JobListener {

	private final String name ;
	private final Long jobId ;
	private final IJobInstanceService jobInstanceService ;

	public PipelineJobListener(String name, Long jobId , IJobInstanceService jobInstanceService){
		this.name = name;
		this.jobId = jobId ;
		this.jobInstanceService = jobInstanceService;
	}

	@Override
	public String getName() {
		return name ;
	}

	/**
	 * 作业被触发后，如果触发器触发成功，则调用此方法
	 * @param jobExecutionContext
	 */
	@Override
	public void jobToBeExecuted(JobExecutionContext jobExecutionContext) {
		log.debug("jobToBeExecuted:{} , jobId = {}" , jobExecutionContext.getJobDetail().getKey() , jobId);

		// 输出运行结果
		jobInstanceService.startMonitorJob(jobId) ;
	}

	/**
	 * 作业被触发后，如果触发器触发成功，则调用此方法
	 * @param jobExecutionContext
	 * @param e
	 */
	@Override
	public void jobWasExecuted(JobExecutionContext jobExecutionContext, JobExecutionException e) {
		log.debug("jobWasExecuted:{} , jobId = {}" , jobExecutionContext.getJobDetail().getKey() , jobId);

		String status = JobStatusEnums.FAILED.getStatus();
		String message = null ;

		if(e != null){
			log.error("jobWasExecuted:{} , jobId = {} , error = {}" , jobExecutionContext.getJobDetail().getKey() , jobId , e.getMessage());
			status = JobStatusEnums.FAILED.getStatus();
			message = e.getMessage() ;
		}

		jobInstanceService.finishMonitorJob(jobId , status , message) ;
	}

	/**
	 * 作业被触发后，如果触发器触发失败，则调用此方法
	 * @param jobExecutionContext
	 */
	@Override
	public void jobExecutionVetoed(JobExecutionContext jobExecutionContext) {

	}

}