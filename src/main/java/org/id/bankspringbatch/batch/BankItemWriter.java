package org.id.bankspringbatch.batch;

import org.id.bankspringbatch.model.BankTransaction;
import org.id.bankspringbatch.repository.BankTransactionRepository;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BankItemWriter implements ItemWriter<BankTransaction> {
    @Autowired
    private BankTransactionRepository bankTransactionRepository;
    @Override
    public void write(List<? extends BankTransaction> transactions) throws Exception {
        bankTransactionRepository.saveAll(transactions);

    }
}
