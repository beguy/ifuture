package com.github.beguy.ifuture_server.repository;

import com.github.beguy.ifuture_server.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {
    @Modifying
    @Query("update Account a set a.amount = a.amount + :value where a.id=:id")
    void addAmount(@Param("id") Integer id, @Param("value") Long value);
}
