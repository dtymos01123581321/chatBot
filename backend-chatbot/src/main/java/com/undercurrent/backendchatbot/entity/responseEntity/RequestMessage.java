package com.undercurrent.backendchatbot.entity.responseEntity;

public class RequestMessage {
    private String msg;
    private Integer step;

    public Integer getStep() {
        return step;
    }

    public void setStep(Integer step) {
        this.step = step;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
