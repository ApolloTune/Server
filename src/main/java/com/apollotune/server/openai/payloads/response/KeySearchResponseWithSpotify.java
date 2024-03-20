package com.apollotune.server.openai.payloads.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KeySearchResponseWithSpotify {
    private String songName;
    private String songArtist;
    private String songPhoto;
    private String spotifyLink;
}
