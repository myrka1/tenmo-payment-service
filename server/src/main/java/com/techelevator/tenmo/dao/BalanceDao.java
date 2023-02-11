package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exceptions.AmountExceedsBalanceException;
import com.techelevator.tenmo.model.Transaction;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.math.BigDecimal;
import java.util.List;

public interface BalanceDao {

    BigDecimal viewBalance(String currentUser) throws UsernameNotFoundException, com.techelevator.tenmo.exceptions.UsernameNotFoundException;
    List<Transaction> viewTransactionHistory(String currentUser) throws UsernameNotFoundException;
    void sendBucks(Transaction transaction, String currentUser) throws UsernameNotFoundException, AmountExceedsBalanceException, AmountExceedsBalanceException, com.techelevator.tenmo.exceptions.UsernameNotFoundException;
}
