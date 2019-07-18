package com.github.beguy.ifuture_server.repository;

import com.github.beguy.ifuture_server.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {
}
