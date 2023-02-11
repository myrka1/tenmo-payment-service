package com.techelevator.tenmo.model;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

public class Transaction {
    @NotNull(message = "The `transferId` field cannot be null.")
    private int transferId;

    @NotNull(message = "The `transferTypeId` field cannot be null.")
    private int transferTypeId;

    @NotNull(message = "The `transferStatusId` field cannot be null.")
    private int transferStatusId;

    @NotNull(message = "The `userFrom` field cannot be null.")
    private int userFrom;

    @NotNull(message = "The `userTo` field cannot be null.")
    private int userTo;

    @Positive(message = "The `amount` field cannot be zero or negative.")
    private BigDecimal amount;

    @NotNull(message = "The `userFromName` field cannot be null.")
    private String userFromName;

    @NotNull(message = "The `userToName` field cannot be null.")
    private String userToName;

    @NotNull(message = "The `transferTypeDesc field cannot be null.")
    private String transferTypeDesc;

    @NotNull(message = "The `transferStatusDesc` field cannot be null.")
    private String transferStatusDesc;
    public Transaction(){
    }
    public String getTransferTypeDesc() {
        return transferTypeDesc;
    }
    public void setTransferTypeDesc(String transferTypeDesc) {
        this.transferTypeDesc = transferTypeDesc;
    }
    public String getTransferStatusDesc() {
        return transferStatusDesc;
    }
    public void setTransferStatusDesc(String transferStatusDesc) {
        this.transferStatusDesc = transferStatusDesc;
    }
    public String getUserFromName() {
        return userFromName;
    }
    public void setUserFromName(String userFromName) {
        this.userFromName = userFromName;
    }
    public String getUserToName() {
        return userToName;
    }
    public void setUserToName(String userToName) {
        this.userToName = userToName;
    }
    public int getTransferId() {
        return transferId;
    }
    public void setTransferId(int transferId) {
        this.transferId = transferId;
    }
    public int getTransferTypeId() {
        return transferTypeId;
    }
    public void setTransferTypeId(int transferTypeId) {
        this.transferTypeId = transferTypeId;
    }
    public int getTransferStatusId() {
        return transferStatusId;
    }
    public void setTransferStatusId(int transferStatusId) {
        this.transferStatusId = transferStatusId;
    }
    public int getUserFrom() {
        return userFrom;
    }
    public void setUserFrom(int userFrom) {
        this.userFrom = userFrom;
    }
    public int getUserTo() {
        return userTo;
    }
    public void setUserTo(int userTo) {
        this.userTo = userTo;
    }
    public BigDecimal getAmount() {
        return amount;
    }
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
