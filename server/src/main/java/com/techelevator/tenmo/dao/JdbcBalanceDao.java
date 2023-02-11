package com.techelevator.tenmo.dao;
import com.techelevator.tenmo.exceptions.AmountExceedsBalanceException;
import com.techelevator.tenmo.exceptions.UsernameNotFoundException;
import com.techelevator.tenmo.model.Transaction;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
@Component
public class JdbcBalanceDao implements BalanceDao{
    private  JdbcTemplate jdbcTemplate;

    public JdbcBalanceDao(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }
    @Override
    public BigDecimal viewBalance(String currentUser) throws UsernameNotFoundException {
        BigDecimal amount = new BigDecimal("0");
        String sql = "SELECT balance FROM account JOIN tenmo_user ON tenmo_user.user_id = account.user_id " +
                "WHERE username = ?;";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql,currentUser);
        if(result.next()){
            amount = result.getBigDecimal("balance");
        }else{
            throw new UsernameNotFoundException();
        }
        return amount;


    }
    @Override
    public List<Transaction> viewTransactionHistory(String currentUser){
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT DISTINCT transfer_id, transfer.transfer_type_id, transfer.transfer_status_id, account_from, account_to, amount FROM transfer " +
                "JOIN transfer_type ON transfer.transfer_type_id = transfer_type.transfer_type_id " +
                "JOIN transfer_status ON transfer.transfer_status_id = transfer.transfer_status_id " +
                "LEFT JOIN account ON account.account_id = transfer.account_from " +
                "LEFT JOIN tenmo_user ON tenmo_user.user_id = account.account_id " +
                "WHERE account_from = (SELECT account_id FROM account JOIN tenmo_user ON tenmo_user.user_id = account.user_id WHERE username = ?) " +
                "OR account_to = (SELECT account_id FROM account JOIN tenmo_user ON tenmo_user.user_id = account.user_id WHERE username = ?)" +
                "AND transfer_status_desc = 'Approved'";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, currentUser, currentUser);
        while(results.next()){
            Transaction t = mapRowToTransaction(results);
            String userFromSql = "SELECT username FROM tenmo_user JOIN account ON tenmo_user.user_id = account.user_id " +
                    "WHERE account.account_id = ?";
            SqlRowSet userFromResult = jdbcTemplate.queryForRowSet(userFromSql, t.getUserFrom());
            if(userFromResult.next()){
                t.setUserFromName(userFromResult.getString("username"));
            }
            SqlRowSet userToResult = jdbcTemplate.queryForRowSet(userFromSql, t.getUserTo());
            if(userToResult.next()){
                t.setUserToName(userToResult.getString("username"));
            }
            String transferTypeSql = "SELECT transfer_type_desc FROM transfer_type WHERE transfer_type_id = ?";
            SqlRowSet transferTypeResult = jdbcTemplate.queryForRowSet(transferTypeSql, t.getTransferTypeId());
            if(transferTypeResult.next()){
                t.setTransferTypeDesc(transferTypeResult.getString("transfer_type_desc"));
            }
            String transferStatusSql = "SELECT transfer_status_desc FROM transfer_status WHERE transfer_status_id = ?";
            SqlRowSet transferStatusResult = jdbcTemplate.queryForRowSet(transferStatusSql, t.getTransferStatusId());
            if(transferStatusResult.next()){
                t.setTransferStatusDesc(transferStatusResult.getString("transfer_status_desc"));
            }
            transactions.add(t);
        }
        return transactions;
    }

    @Override
    public void sendBucks(Transaction transaction, String currentUser) throws AmountExceedsBalanceException, UsernameNotFoundException {
        try {
            if(transaction.getAmount().doubleValue() <= viewBalance(currentUser).doubleValue()){
                String sql = "INSERT INTO transfer (transfer_type_id, transfer_status_id, account_from, " +
                        "account_to, amount) VALUES (?, ?, " + "(SELECT account.account_id FROM account JOIN tenmo_user " +
                        "ON tenmo_user.user_id = account.user_id " +
                        "WHERE username = ?), (SELECT account.account_id FROM account JOIN tenmo_user ON " +
                        "tenmo_user.user_id = account.user_id WHERE tenmo_user.user_id = ?), ?) RETURNING transfer_id;";
                Integer id = jdbcTemplate.queryForObject(sql, Integer.class, transaction.getTransferTypeId(),
                        transaction.getTransferStatusId(),currentUser,transaction.getUserTo(),transaction.getAmount());
                transaction.setTransferId(id);
                String increaseBalanceSql = "UPDATE account SET balance = balance + ? WHERE account_id = (SELECT account.account_id FROM account " +
                        "JOIN tenmo_user ON tenmo_user.user_id = account.user_id WHERE tenmo_user.user_id = ?);";
                jdbcTemplate.update(increaseBalanceSql, transaction.getAmount(), transaction.getUserTo());
                String decreaseBalanceSql = "UPDATE account SET balance = balance - ? WHERE account_id = (SELECT account.account_id FROM account " +
                        "JOIN tenmo_user ON tenmo_user.user_id = account.user_id WHERE username = ?);";
                jdbcTemplate.update(decreaseBalanceSql, transaction.getAmount(), currentUser);
            }
            throw new AmountExceedsBalanceException();
        }catch(AmountExceedsBalanceException e1){
            throw new UsernameNotFoundException();
        } catch (UsernameNotFoundException e) {
        }
    }

    private Transaction mapRowToTransaction(SqlRowSet rowSet){
        Transaction transaction = new Transaction();
        transaction.setTransferId(rowSet.getInt("transfer_id"));
        transaction.setTransferTypeId(rowSet.getInt("transfer_type_id"));
        transaction.setTransferStatusId((rowSet.getInt("transfer_status_id")));
        transaction.setUserFrom(rowSet.getInt("account_from"));
        transaction.setUserTo(rowSet.getInt("account_to"));
        transaction.setAmount(rowSet.getBigDecimal("amount"));
        return transaction;
    }
}
