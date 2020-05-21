package com.example.carrobot.listener;

import com.example.carrobot.model.Car;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class JobListener extends JobExecutionListenerSupport {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobListener.class);
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public JobListener(JdbcTemplate jdbcTemplate) {
        super();
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void beforeJob(JobExecution jobExecution) {
        LOGGER.info("JOB STARTING...");
        LOGGER.info("Deleting rows in DB if exist...");
        jdbcTemplate.execute("DELETE FROM cars WHERE id > 4");
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            LOGGER.info("JOB COMPLETED");
        }
        jdbcTemplate.query("SELECT * FROM cars",
                (rs, row) -> new Car(
                        rs.getLong(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getDouble(5)))
                .forEach(car -> LOGGER.info(String.format("Recorded %s ", car)));

        Integer total = jdbcTemplate.queryForObject("SELECT count(*) FROM cars", new Object[]{}, Integer.class);
        LOGGER.info(String.format("Total rows in DB: %d", total));
    }

}
