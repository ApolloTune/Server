package com.apollotune.server.services;

import com.apollotune.server.entities.Favsong;
import com.apollotune.server.payloads.request.FavsongRequest;
import com.apollotune.server.payloads.response.ApiResponse;

import java.util.List;

public interface FavsongService {
    ApiResponse addFavsong(FavsongRequest favsongRequest);

    ApiResponse deleteFavsong(Integer id);

    List<Favsong> getAllFavsong();

    List<Favsong> getFavsongOfUser(Integer userId);

    List<Favsong> getFavsongOfSong(Integer songId);

}
