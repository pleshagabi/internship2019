package model;

import org.joda.time.LocalDate;

public class CreditCard {

    private CardType cardType;
    private double fee;
    private int withdrawLimit;
    private LocalDate expirationDate;
    private int availableAmount;


    public CreditCard(){

    }

    public CreditCard(CardType cardType, double fee, int withdrawLimit, LocalDate expirationDate, int availableAmount) {
        this.cardType = cardType;
        this.fee = fee;
        this.withdrawLimit = withdrawLimit;
        this.expirationDate = expirationDate;
        this.availableAmount = availableAmount;
    }

    public CardType getCardType() {
        return cardType;
    }

    public void setCardType(CardType cardType) {
        this.cardType = cardType;
    }

    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

    public int getWithdrawLimit() {
        return withdrawLimit;
    }

    public void setWithdrawLimit(int withdrawLimit) {
        this.withdrawLimit = withdrawLimit;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public int getAvailableAmount() {
        return availableAmount;
    }

    public void setAvailableAmount(int availableAmount) {
        this.availableAmount = availableAmount;
    }

    @Override
    public String toString() {
        return "CreditCard{" +
                "cardType=" + cardType +
                ", fee=" + fee +
                ", withdrawLimit=" + withdrawLimit +
                ", expirationDate=" + expirationDate +
                ", availableAmount=" + availableAmount +
                '}';
    }
}
