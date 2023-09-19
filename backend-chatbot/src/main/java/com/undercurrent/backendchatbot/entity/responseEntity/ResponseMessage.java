package com.undercurrent.backendchatbot.entity.responseEntity;

public class ResponseMessage {
    private String msg;
    private String[] nextSteps;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String[] getNextSteps() {
        return nextSteps;
    }

    public void setNextSteps(String[] nextSteps) {
        this.nextSteps = nextSteps;
    }
}
