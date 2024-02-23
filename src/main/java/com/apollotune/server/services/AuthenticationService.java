package com.apollotune.server.services;

import com.apollotune.server.payloads.request.AuthenticationRequest;
import com.apollotune.server.payloads.response.AuthenticationResponse;
import com.apollotune.server.payloads.request.RegisterRequest;

public interface AuthenticationService {
    // AuthenticationResponse Token returned payload
    AuthenticationResponse register(RegisterRequest request);

    AuthenticationResponse login(AuthenticationRequest request);
}
