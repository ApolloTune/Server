package com.apollotune.server.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;

public interface BraveSearchService {
    ResponseEntity<String> performSearch(String musicName, String artistName);
}
