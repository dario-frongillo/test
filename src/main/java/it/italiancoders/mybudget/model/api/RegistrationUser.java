package it.italiancoders.mybudget.model.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Email;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Collection;

@Builder(builderMethodName = "newBuilder")
@Getter
@Setter
@AllArgsConstructor
public class RegistrationUser  {

    @Size(min = 4, max = 100)
    @NotNull
    private String username;

    @JsonIgnore
    @NotNull
    private String password;

    @Email
    @NotNull
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String email;

    @NotNull
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String firstname;

    @NotNull
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String lastname;


    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private GenderEnum gender;

    public RegistrationUser(){}


}
