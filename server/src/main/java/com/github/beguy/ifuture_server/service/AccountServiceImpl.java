package com.github.beguy.ifuture_server.service;

import com.github.beguy.ifuture_server.model.Account;
import com.github.beguy.ifuture_server.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    AccountRepository accountRepository;

    @Override
    @Transactional(readOnly = true)
    public Long getAmount(Integer id) {
        Optional<Account> account = accountRepository.findById(id);
        return account
                .map(Account::getAmount)
                .orElse(null);
    }

    @Override
    @Transactional
    public void addAmount(Integer id, Long value) {
        Optional<Account> accountOptional = accountRepository.findById(id);
        accountOptional.ifPresent(account -> {
            account.setAmount(account.getAmount() + value);
            accountRepository.saveAndFlush(account);
        });
        accountOptional.orElseGet(()->accountRepository.save(new Account(id, value)));
    }
}
