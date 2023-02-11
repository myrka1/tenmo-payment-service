package com.techelevator.tenmo.exceptions;

public class AmountExceedsBalanceException extends Exception{

    private String errorMessage = "Amount exceeds balance! Enter valid amount!";
    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
