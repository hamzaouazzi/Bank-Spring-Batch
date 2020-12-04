package org.id.bankspringbatch.config;

import org.id.bankspringbatch.model.BankTransaction;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;


@Configuration
@EnableBatchProcessing
public class SpringBatchConfiguration {
    @Autowired private JobBuilderFactory jobBuilderFactory;
    @Autowired private StepBuilderFactory stepBuilderFactory;
    @Autowired private ItemReader<BankTransaction> bankTransactionItemReader;
    @Autowired private ItemWriter<BankTransaction> bankTransactionItemWriter;
    @Autowired private ItemProcessor<BankTransaction,BankTransaction> bankTransactionitemProcessor;
    @Bean
    public Job myJob(){
        Step step=stepBuilderFactory.get("ETL-Transaction-File-Load")
                .<BankTransaction,BankTransaction>chunk(100)
                .reader(bankTransactionItemReader)
                .writer(bankTransactionItemWriter)
                .processor(bankTransactionitemProcessor)
                .build();
        return jobBuilderFactory.get("ETL-Load")
                .incrementer(new RunIdIncrementer())
                .start(step)
                .build();
    }
    @Bean
    public FlatFileItemReader<BankTransaction> getItemReader(@Value("${inputFile}") Resource resource){
        FlatFileItemReader<BankTransaction> flatFileItemReader=new FlatFileItemReader<>();
        flatFileItemReader.setName("CSV-READER");
        flatFileItemReader.setLinesToSkip(1);
        flatFileItemReader.setResource(resource);
        flatFileItemReader.setLineMapper(lineMapper());
        return flatFileItemReader;
    }
    @Bean
    public LineMapper<BankTransaction> lineMapper(){
        DefaultLineMapper<BankTransaction> lineMapper=new DefaultLineMapper<>();
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(",");
        lineTokenizer.setNames("id","accountID","strDate","transactionType","amount");
        lineMapper.setLineTokenizer(lineTokenizer);
        BeanWrapperFieldSetMapper<BankTransaction> fieldSetMapper=new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(BankTransaction.class);
        lineMapper.setFieldSetMapper(fieldSetMapper);
        return lineMapper;
    }
    
}
