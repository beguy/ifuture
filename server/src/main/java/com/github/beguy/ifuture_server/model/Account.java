package com.github.beguy.ifuture_server.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Account {
    @Id
    private int id;

    @Column
    private long amount = 0;

    public Account() {
    }

    public Account(int id, long amount) {
        this.id = id;
        this.amount = amount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Account account = (Account) o;

        if (id != account.id) return false;
        return amount == account.amount;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (int) (amount ^ (amount >>> 32));
        return result;
    }
}