package com.apollotune.server.services;

import com.apollotune.server.payloads.AuthenticationRequest;
import com.apollotune.server.payloads.AuthenticationResponse;
import com.apollotune.server.payloads.RegisterRequest;

public interface AuthenticationService {
    // AuthenticationResponse Token returned payload
    AuthenticationResponse register(RegisterRequest request);

    AuthenticationResponse login(AuthenticationRequest request);
}
