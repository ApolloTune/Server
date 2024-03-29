package com.apollotune.server.openai.payloads.request;


import com.apollotune.server.openai.enums.MusicEmotion;
import com.apollotune.server.openai.enums.MusicLanguages;
import com.apollotune.server.openai.enums.MusicType;
import com.apollotune.server.openai.enums.MusicYear;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class KeySearchRequest {
    private List<MusicEmotion> emotions;
    private List<MusicType> musicTypes;
    private List<MusicYear> musicYears;
    private List<MusicLanguages> musicLanguages;

    public KeySearchRequest(){
        emotions = new ArrayList<>();
        musicTypes = new ArrayList<>();
        musicYears = new ArrayList<>();
        musicLanguages = new ArrayList<>();
    }

}
