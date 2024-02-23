package com.apollotune.server.payloads.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SongRequest {
    private String songname;

    private String songartist;

    private String songphoto;

    private String spotifylink;

    private String ytmusiclink;
}
