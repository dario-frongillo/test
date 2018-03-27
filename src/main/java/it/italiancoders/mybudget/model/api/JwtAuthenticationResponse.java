package it.italiancoders.mybudget.model.api;

import lombok.Builder;
import lombok.Data;

@Builder(builderMethodName = "newBuilder")
@Data
public class JwtAuthenticationResponse {
    private User user;
    private String refreshToken;
}
