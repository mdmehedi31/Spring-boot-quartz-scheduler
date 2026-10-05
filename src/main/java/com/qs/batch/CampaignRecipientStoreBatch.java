package com.qs.batch;


import ch.qos.logback.classic.Logger;
import com.qs.dto.BatchTestDTO;
import org.jspecify.annotations.Nullable;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.listener.StepExecutionListener;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.StepExecution;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.item.ItemReader;
import org.springframework.batch.infrastructure.item.ItemWriter;
import org.springframework.batch.infrastructure.item.database.JdbcBatchItemWriter;
import org.springframework.batch.infrastructure.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.infrastructure.item.file.FlatFileItemReader;
import org.springframework.batch.infrastructure.item.file.LineMapper;
import org.springframework.batch.infrastructure.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.infrastructure.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.infrastructure.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.infrastructure.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.infrastructure.item.support.ListItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import javax.sql.DataSource;
import java.time.LocalTime;
import java.util.List;

@Configuration
public class CampaignRecipientStoreBatch {

    private static final Logger log = (Logger) LoggerFactory.getLogger(CampaignRecipientStoreBatch.class);

    @Bean
    @StepScope
    public ItemReader<BatchTestDTO> campaignDataReader(
                       @Value("#{jobParameters['campaignId']}") String campaignId) {
        log.info("Reader :: campaignId: " + campaignId);
        return new ListItemReader<>(List.of(new BatchTestDTO("Test campaign id is " + campaignId)));
    }

    @Bean
    @StepScope
    public CampaignRecipientStoreProcessor processor(@Value("#{jobParameters['campaignId']}") String campaignId) {
        return new CampaignRecipientStoreProcessor(Integer.valueOf(campaignId));
    }

    @Bean
    @StepScope
    public ItemWriter<BatchTestDTO> writer() {

        return chunk -> {
            for (BatchTestDTO item : chunk) {
                log.info("Writer received: {}", item);
            }
        };
    }

    @Bean
    public Job job(JobRepository jobRepository, Step step, TempFileCleanListener tempFileCleanListener) {

        return new JobBuilder("emailRecipientCamp",jobRepository)
                .listener(tempFileCleanListener)
                .start(step).build();
    }

    @Bean
    public Step step(JobRepository jobRepository,
                     ItemReader<BatchTestDTO> reader,
                     ItemWriter<BatchTestDTO> writer,
                     CampaignRecipientStoreProcessor processor) {

        return new StepBuilder("emailRecipientCampStep",jobRepository)
                .<BatchTestDTO,BatchTestDTO>chunk(1000)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .listener(new StepExecutionListener() {
                    @Override
                    public @Nullable ExitStatus afterStep(StepExecution stepExecution) {
                        String campaignId = stepExecution
                                .getJobParameters()
                                .getString("campaignId");

                        log.info(
                                "========== END ==========" +
                                        " campaignId={} thread={} time={} status={}",
                                campaignId,
                                Thread.currentThread().getName(),
                                LocalTime.now(),
                                stepExecution.getStatus()
                        );

                        return stepExecution.getExitStatus();
                    }

                    @Override
                    public void beforeStep(StepExecution stepExecution) {
                        String campaignId = stepExecution
                                .getJobParameters()
                                .getString("campaignId");

                        log.info(
                                "========== START ==========" +
                                        " campaignId={} thread={} time={}",
                                campaignId,
                                Thread.currentThread().getName(),
                                LocalTime.now()
                        );
                    }
                })
                .build();

    }
}
