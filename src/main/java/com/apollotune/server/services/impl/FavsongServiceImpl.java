package com.apollotune.server.services.impl;

import com.apollotune.server.entities.Favsong;
import com.apollotune.server.exceptions.ApiException;
import com.apollotune.server.payloads.request.FavsongRequest;
import com.apollotune.server.payloads.response.ApiResponse;
import com.apollotune.server.repositories.FavsongRepository;
import com.apollotune.server.repositories.SongRepository;
import com.apollotune.server.repositories.UserRepository;
import com.apollotune.server.services.FavsongService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FavsongServiceImpl implements FavsongService {
    private final FavsongRepository repository;
    private final SongRepository songRepository;
    private final UserRepository userRepository;
    @Override
    public ApiResponse addFavsong(FavsongRequest favsongRequest) {
        var user = userRepository.findById(favsongRequest.getUserId())
                .orElseThrow(() -> new ApiException("No user found with ID: " + favsongRequest.getUserId()));
        var song = songRepository.findById(favsongRequest.getSongId())
                .orElseThrow(() -> new ApiException("No song found with ID: " + favsongRequest.getSongId()));

        Favsong favsong = Favsong.builder().user(user).song(song).build();
        if(user.getFavsongs().contains(favsong)){
            throw new ApiException("this song is in the user's favorite list");
        }
        user.add(favsong);
        song.addFavsong(favsong);

        userRepository.save(user);
        songRepository.save(song);

        return ApiResponse.builder().message("FavSong added").success(true).build();
    }

    @Override
    public ApiResponse deleteFavsong(Integer id) {
        var favSong = repository.findById(id)
                .orElseThrow(() -> new ApiException("No favorite song with ID: " + id));
        repository.delete(favSong);
        return ApiResponse.builder()
                .message("Favorite song deleted")
                .success(true)
                .build();
    }

    @Override
    public List<Favsong> getAllFavsong() {
        List<Favsong> allFavSong = repository.findAll();
        return allFavSong;
    }

    @Override
    public List<Favsong> getFavsongOfUser(Integer userId) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException("No user found with ID: " + userId));
        List<Favsong> allFavSong = user.getFavsongs();
        return allFavSong;
    }

    @Override
    public List<Favsong> getFavsongOfSong(Integer songId) {
        var song = songRepository.findById(songId)
                .orElseThrow(() -> new ApiException("No song found with ID: " + songId));
        List<Favsong> allFavSong = song.getFavsongs();
        return allFavSong;
    }
}
