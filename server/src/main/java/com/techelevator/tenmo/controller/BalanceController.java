package com.techelevator.tenmo.controller;
import com.techelevator.tenmo.dao.BalanceDao;
import com.techelevator.tenmo.exceptions.AmountExceedsBalanceException;
import com.techelevator.tenmo.exceptions.UsernameNotFoundException;
import com.techelevator.tenmo.model.Transaction;
import com.techelevator.tenmo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
@PreAuthorize("isAuthenticated()")
@RestController
public class BalanceController {

    @Autowired
    private BalanceDao dao;
    @RequestMapping(path = "/balances", method = RequestMethod.GET)
    public BigDecimal viewBalance(Principal principal){
        BigDecimal balance = new BigDecimal("0");
        try {
            balance =  dao.viewBalance(principal.getName());
        }catch(UsernameNotFoundException e){
            e.getMessage();
        }
        return balance;
    }

    @RequestMapping(path = "/transfers", method = RequestMethod.GET)
    public List<Transaction> viewTransactionHistory(Principal principal) throws UsernameNotFoundException {
        List<Transaction> transactions = new ArrayList<>();
        transactions = dao.viewTransactionHistory(principal.getName());
        return transactions;
    }
    @RequestMapping(path = "/transfers", method = RequestMethod.POST)
    public void sendBucks(@RequestBody Transaction transaction, Principal principal){
        try{
            dao.sendBucks(transaction, principal.getName());
        }catch (UsernameNotFoundException e){
            e.getMessage();
        }catch (AmountExceedsBalanceException e1){
            e1.getErrorMessage();
        }
    }
}
