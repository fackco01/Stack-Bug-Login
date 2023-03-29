package com.mesqueungroupe.stackbugv1.controller;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthenticationResponse {
    private String token;
    private String refresh;

    @Override
    public String toString() {
        return "token=" + token + '-' + "refresh=" + refresh;
    }
}
