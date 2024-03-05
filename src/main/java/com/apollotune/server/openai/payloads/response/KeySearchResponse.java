package com.apollotune.server.openai.payloads.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KeySearchResponse {
    private String artistName;
    private String musicName;
    private String genre;
    private String language;
}
