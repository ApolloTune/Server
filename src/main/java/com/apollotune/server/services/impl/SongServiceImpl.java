package com.apollotune.server.services.impl;

import com.apollotune.server.entities.Song;
import com.apollotune.server.exceptions.ApiException;
import com.apollotune.server.payloads.request.SongRequest;
import com.apollotune.server.payloads.response.ApiResponse;
import com.apollotune.server.payloads.response.SongResponse;
import com.apollotune.server.repositories.SongRepository;
import com.apollotune.server.services.SongService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SongServiceImpl implements SongService {
    private final SongRepository repository;
    @Override
    public ApiResponse addSong(SongRequest songRequest) {
        try{
            var song = Song.builder()
                    .songname(songRequest.getSongname())
                    .songartist(songRequest.getSongartist())
                    .songphoto(songRequest.getSongphoto())
                    .spotifylink(songRequest.getSpotifylink())
                    .ytmusiclink(songRequest.getYtmusiclink())
                    .build();
            repository.save(song);
            return ApiResponse.builder()
                    .message("Song created")
                    .success(true)
                    .build();
        }catch (Exception e){
            return ApiResponse.builder()
                    .message("Song not created: "+e.getMessage())
                    .success(false)
                    .build();
        }
    }
    @Override
    public ApiResponse deleteSong(Integer id) {
        var song = repository.findById(id)
                .orElseThrow(() -> new ApiException("No songs with "+id+" found"));
        repository.delete(song);
        return ApiResponse.builder()
                .message("deleted song with "+id)
                .success(true)
                .build();
    }

    @Override
    public ApiResponse updateSong(SongRequest songRequest, Integer id) {
        var song = repository.findById(id)
                .orElseThrow(() -> new ApiException("No songs with "+id+" found"));
        song.setSongname(songRequest.getSongname());
        song.setSongartist(songRequest.getSongartist());
        song.setSongphoto(songRequest.getSongphoto());
        song.setSpotifylink(songRequest.getSpotifylink());
        song.setYtmusiclink(songRequest.getYtmusiclink());
        repository.save(song);
        return ApiResponse.builder()
                .message("updated song with "+id)
                .success(true)
                .build();
    }

    @Override
    public List<Song> getAllSong() {
        List<Song> allsong = repository.findAll();
        return allsong;
    }

    @Override
    public SongResponse getSong(Integer id) {
        return null;
    }

    @Override
    public SongResponse getSongByName(String name) {
        return null;
    }
}
