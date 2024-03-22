package com.apollotune.server.openai.payloads.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class FavoriteSearchRequest {
    private List<String> artistName;
    private List<String> musicName;
    public FavoriteSearchRequest(){
        artistName = new ArrayList<>();
        musicName = new ArrayList<>();
    }
}
