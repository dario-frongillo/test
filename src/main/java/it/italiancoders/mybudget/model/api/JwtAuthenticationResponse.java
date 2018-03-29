package it.italiancoders.mybudget.model.api;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Builder(builderMethodName = "newBuilder")
@Data
public class JwtAuthenticationResponse {
    private User user;
    private String refreshToken;
    private  List<Account> accounts = new ArrayList<>();
}
