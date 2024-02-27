package com.apollotune.server.services;

import com.apollotune.server.entities.Song;
import com.apollotune.server.payloads.response.ApiResponse;
import com.apollotune.server.payloads.request.SongRequest;
import com.apollotune.server.payloads.response.SongResponse;

import java.util.List;

public interface SongService {
    // Todo: Burada sarkilara crud islemi gerceklestirecek metodlari yazacagiz.
    ApiResponse addSong(SongRequest songRequest);

    ApiResponse deleteSong(Integer id);

    ApiResponse updateSong(SongRequest songRequest, Integer id);

    List<Song> getAllSong();

    SongResponse getSong(Integer id);

    SongResponse getSongByName(String name);

}
