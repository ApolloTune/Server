package com.apollotune.server.gemini.payloads.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GeminiKeySearchResponse {
    private String artistName;
    private String musicName;
    private String genre;
    private String language;
    private String spotifyId;
}
