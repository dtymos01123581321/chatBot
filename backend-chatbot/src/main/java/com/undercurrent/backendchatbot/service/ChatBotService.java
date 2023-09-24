package com.undercurrent.backendchatbot.service;

import com.undercurrent.backendchatbot.entity.responseEntity.ResponseMessage;
import com.undercurrent.backendchatbot.entity.responseEntity.RequestMessage;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ChatBotService {
    public ResponseMessage getResponseMessage(RequestMessage data, List<String>  queries) {
        String nextQuery = queries.get(data.getStep());

        String[] parts = nextQuery.split("#", 2);
        String nextMsg = parts[0].trim();
        String stepsString = parts[1];

        Pattern pattern = Pattern.compile("(\\w+)-(\\d+)");
        Matcher matcher = pattern.matcher(stepsString);

        List<ResponseMessage.NextStep> nextSteps = new ArrayList<>();
        while (matcher.find()) {
            String type = matcher.group(1);
            int step = Integer.parseInt(matcher.group(2));
            nextSteps.add(new ResponseMessage.NextStep(type, step));
        }

        return new ResponseMessage(nextMsg, nextSteps);
    }
}
