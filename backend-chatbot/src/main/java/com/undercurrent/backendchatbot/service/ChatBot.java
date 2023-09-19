package com.undercurrent.backendchatbot.service;

import com.undercurrent.backendchatbot.entity.responseEntity.ResponseMessage;
import com.undercurrent.backendchatbot.entity.responseEntity.RequestMessage;
import java.util.Arrays;
import java.util.List;

public class ChatBot {

    public static ResponseMessage getResponseMessage(RequestMessage msg, List<String>  queries) {
        String nextQuery = queries.get(msg.getStep());
        ResponseMessage responseMessage = new ResponseMessage();
        String[] parts = nextQuery.split("#");
        responseMessage.setMsg(parts[0]);
        String[] nextSteps = Arrays.copyOfRange(parts, 1, parts.length);
        responseMessage.setNextSteps(nextSteps);
        return responseMessage;
    }
}
