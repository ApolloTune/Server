package com.apollotune.server.gemini.payloads.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class GeminiFavoriteSearchRequest {
    private List<String> artistName;
    private List<String> musicName;
    public GeminiFavoriteSearchRequest(){
        artistName = new ArrayList<>();
        musicName = new ArrayList<>();
    }
}
