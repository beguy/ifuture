package com.github.beguy.ifuture_server.service.impl;

import com.github.beguy.ifuture_server.model.Account;
import com.github.beguy.ifuture_server.repository.AccountRepository;
import com.github.beguy.ifuture_server.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    @Cacheable("amounts")
    @Transactional(readOnly = true)
    public Long getAmount(Integer id) {
        return accountRepository.findById(id)
                .map(Account::getAmount)
                .orElse(null);
    }

    @Override
    @Transactional
    @CacheEvict(value = "amounts", key = "#id")
    public void addAmount(Integer id, Long value) {
        Optional<Account> accountOptional = accountRepository.findById(id);
        accountOptional.ifPresent(account -> {
            account.setAmount(account.getAmount() + value);
            accountRepository.saveAndFlush(account);
        });
        accountOptional.orElseGet(()->accountRepository.save(new Account(id, value)));
    }
}
