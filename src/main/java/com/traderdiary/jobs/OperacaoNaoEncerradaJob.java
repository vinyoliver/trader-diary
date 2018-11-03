package com.traderdiary.jobs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Schedule;
import javax.ejb.Singleton;

@Singleton
public class OperacaoNaoEncerradaJob {

    public static final Logger LOGGER = LoggerFactory.getLogger(OperacaoNaoEncerradaJob.class);


    @Schedule(hour = "0", dayOfWeek = "*")
    public void notificarUsuarios() {
        LOGGER.info("Inicio JOB: OperacaoNaoEncerradaJob");
        //TODO: Notify users about operations that has been opened more than 90 days ago.
        LOGGER.info("Fim JOB: OperacaoNaoEncerradaJob");
    }


}
