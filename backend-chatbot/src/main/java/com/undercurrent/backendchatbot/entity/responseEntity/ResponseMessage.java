package com.undercurrent.backendchatbot.entity.responseEntity;

import java.util.List;

public class ResponseMessage {
    private String msg;
    private List<NextStep> nextSteps;

    public ResponseMessage(String msg, List<NextStep> nextSteps) {
        this.msg = msg;
        this.nextSteps = nextSteps;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<NextStep> getNextSteps() {
        return nextSteps;
    }

    public void setNextSteps(List<NextStep> nextSteps) {
        this.nextSteps = nextSteps;
    }

    @Override
    public String toString() {
        return "ResponseMessage{" +
                "msg='" + msg + '\'' +
                ", nextSteps=" + nextSteps +
                '}';
    }

    public static class NextStep {
        private String type;
        private int step;

        public NextStep(String type, int step) {
            this.type = type;
            this.step = step;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getStep() {
            return step;
        }

        public void setStep(int step) {
            this.step = step;
        }

        @Override
        public String toString() {
            return "NextStep{" +
                    "type='" + type + '\'' +
                    ", step=" + step +
                    '}';
        }
    }
}

