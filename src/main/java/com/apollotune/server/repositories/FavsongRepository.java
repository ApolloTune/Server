package com.apollotune.server.repositories;

import com.apollotune.server.entities.Favsong;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavsongRepository extends JpaRepository<Favsong, Integer> {
}
