package com.apollotune.server.payloads.response;


import com.apollotune.server.payloads.request.Message;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatResponse {
    private List<Choice> choices;

    public static class Choice{
        private int index;
        private Message message;
    }



}
