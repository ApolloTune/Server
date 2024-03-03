package com.apollotune.server.gemini.payloads.request;

import com.apollotune.server.gemini.enums.GeminiMusicEmotion;
import com.apollotune.server.gemini.enums.GeminiMusicLanguages;
import com.apollotune.server.gemini.enums.GeminiMusicType;
import com.apollotune.server.gemini.enums.GeminiMusicYear;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class GeminiKeySearchRequest {
    private List<GeminiMusicEmotion> emotions;
    private List<GeminiMusicType> musicTypes;
    private List<GeminiMusicYear> musicYears;
    private List<GeminiMusicLanguages> musicLanguages;

    public GeminiKeySearchRequest() {
        emotions = new ArrayList<>();
        musicTypes = new ArrayList<>();
        musicYears = new ArrayList<>();
        musicLanguages = new ArrayList<>();
    }
}
