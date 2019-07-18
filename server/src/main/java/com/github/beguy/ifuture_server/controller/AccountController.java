package com.github.beguy.ifuture_server.controller;

import com.github.beguy.ifuture_server.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/account")
public class AccountController {
    @Autowired
    AccountService accountService;

    @GetMapping("/{id}/amount")
    public ResponseEntity<Long> getAmount(@PathVariable Integer id){
        Long amount = accountService.getAmount(id);
        if (amount == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(amount);
    }

    @PostMapping("/{id}/amount")
    public ResponseEntity<Void> addAmount(@PathVariable Integer id, @RequestParam Long value){
        accountService.addAmount(id, value);
        return ResponseEntity.ok().build();
    }
}
