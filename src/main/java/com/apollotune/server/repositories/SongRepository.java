package com.apollotune.server.repositories;

import com.apollotune.server.entities.Song;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SongRepository extends JpaRepository<Song, Integer> {
    Optional<Song> findBySongname(String name);
}
