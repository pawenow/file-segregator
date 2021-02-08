package com.paweln.filesegregatesystem;

public class Counter {
    Long devFilesAmount;
    Long testFilesAmount;
    Long homeFileAmount;

    public Long getDevFilesAmount() {
        return devFilesAmount;
    }

    public void setDevFilesAmount(Long devFilesAmount) {
        this.devFilesAmount = devFilesAmount;
    }

    public Long getTestFilesAmount() {
        return testFilesAmount;
    }

    public void setTestFilesAmount(Long testFilesAmount) {
        this.testFilesAmount = testFilesAmount;
    }

    public Long getHomeFileAmount() {
        return homeFileAmount;
    }

    public void setHomeFileAmount(Long homeFileAmount) {
        this.homeFileAmount = homeFileAmount;
    }
    public void incrementByOneDev(){
        this.devFilesAmount++;
    }
    public void incrementByOneTest(){
        this.testFilesAmount++;
    }
}
