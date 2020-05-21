package com.example.carrobot.config;

import com.example.carrobot.listener.JobListener;
import com.example.carrobot.model.Car;
import com.example.carrobot.processor.CarItemProcessor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    public DataSource dataSource;

    @Bean
    public FlatFileItemReader<Car> reader() {
        return new FlatFileItemReaderBuilder<Car>()
                .name("carItemReader")
                .resource(new ClassPathResource("car-data.csv"))
                .delimited()
                .names("id", "brand", "model", "color", "price")
                .fieldSetMapper(new BeanWrapperFieldSetMapper<Car>() {{ setTargetType(Car.class); }}).build();
    }

    @Bean
    public CarItemProcessor processor() {
        return new CarItemProcessor();
    }

    @Bean
    public JdbcBatchItemWriter<Car> writer() {
        return new JdbcBatchItemWriterBuilder<Car>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT IGNORE INTO cars(id,brand,model,color,price) VALUES (:id,:brand,:model,:color,:price);")
                .dataSource(dataSource)
                .build();
    }

    @Bean
    public Job importCarJob(JobListener jobListener, Step step) {
        return jobBuilderFactory.get("importCarJob")
                .incrementer(new RunIdIncrementer())
                .listener(jobListener)
                .flow(step)
                .end()
                .build();
    }

    @Bean
    public Step step(JdbcBatchItemWriter<Car> writer) {
        return stepBuilderFactory.get("step")
                .<Car, Car>chunk(10)
                .reader(reader())
                .processor(processor())
                .writer(writer)
                .build();
    }
}
