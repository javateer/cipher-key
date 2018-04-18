package com.javateer.cipherkey;

import javax.inject.Inject;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CipherKeyJob implements Job {

    private static final Logger logger = LoggerFactory.getLogger(CipherKeyJob.class);

    @Inject
    	private CipherKeyService cipherKeyService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        String triggerName = context.getTrigger().getKey().getName();

        String jobName = context.getJobDetail().getKey().getName();

        logger.debug("Quartz {} for {} started.", triggerName, jobName);
        cipherKeyService.reloadCipherKey();
        cipherKeyService.reloadDecoyKey();
        logger.debug("Quartz {} for {} finished.", triggerName, jobName);
    }

}
