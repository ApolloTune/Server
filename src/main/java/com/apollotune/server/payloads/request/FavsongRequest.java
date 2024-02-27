package com.apollotune.server.payloads.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FavsongRequest {
    private Integer songId;
    private Integer userId;
}
