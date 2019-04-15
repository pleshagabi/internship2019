package model;

import org.joda.time.LocalTime;

public class Atm {

    private String name;
    private LocalTime openingTime;
    private LocalTime closingTime;
    private int moneyAmount;
    private boolean isClosed;
    private boolean isVisited;

    public Atm(){

    }

    public Atm(String name, LocalTime openingTime, LocalTime closingTime, int moneyAmount, boolean isClosed, boolean isVisited) {
        this.name = name;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
        this.moneyAmount = moneyAmount;
        this.isClosed = isClosed;
        this.isVisited = isVisited;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalTime getOpeningTime() {
        return openingTime;
    }

    public void setOpeningTime(LocalTime openingTime) {
        this.openingTime = openingTime;
    }

    public LocalTime getClosingTime() {
        return closingTime;
    }

    public void setClosingTime(LocalTime closingTime) {
        this.closingTime = closingTime;
    }

    public int getMoneyAmount() {
        return moneyAmount;
    }

    public void setMoneyAmount(int moneyAmount) {
        this.moneyAmount = moneyAmount;
    }

    public boolean isClosed() {
        return isClosed;
    }

    public void setClosed(boolean closed) {
        isClosed = closed;
    }

    public boolean isVisited() {
        return isVisited;
    }

    public void setVisited(boolean visited) {
        isVisited = visited;
    }

    @Override
    public String toString() {
        return "Atm{" +
                "name='" + name + '\'' +
                ", openingTime=" + openingTime +
                ", closingTime=" + closingTime +
                ", moneyAmount=" + moneyAmount +
                ", isClosed=" + isClosed +
                ", isVisited=" + isVisited +
                '}';
    }
}
