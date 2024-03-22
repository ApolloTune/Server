package com.apollotune.server.gemini.payloads.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GeminiKeySearchResponseWithSpotify {
    private String songName;
    private String songArtist;
    private String songPhoto;
    private String spotifyLink;
}
