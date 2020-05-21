package com.example.carrobot.processor;

import com.example.carrobot.model.Car;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemProcessor;

public class CarItemProcessor implements ItemProcessor<Car, Car> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CarItemProcessor.class);
    private ExecutionContext executionContext;

    @BeforeStep
    public void beforeStep(StepExecution stepExecution) {
        this.executionContext = stepExecution.getExecutionContext();
    }

    @Override
    public Car process(Car car) throws Exception {
        this.executionContext.putInt("rows", this.executionContext.getInt("rows", 0) + 1);
        String brand = car.getBrand().toUpperCase();
        String model = car.getModel().toUpperCase();
        String color = car.getColor().toUpperCase();

        LOGGER.info(String.format("CAR: %s PROCESSED", car.toString()));
        return new Car(car.getId(), brand, model, color, car.getPrice());
    }

    @AfterStep
    public void afterStep(StepExecution stepExecution) {
        LOGGER.info(String.format("PROCESSED ROWS: %d", this.executionContext.getInt("rows")));
    }
}
