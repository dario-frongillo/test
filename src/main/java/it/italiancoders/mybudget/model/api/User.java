package it.italiancoders.mybudget.model.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Email;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Collection;

@Builder(builderMethodName = "newBuilder")
@NoArgsConstructor
@Data
public class User implements UserDetails {

    @Size(min = 4, max = 100)
    @NotNull
    private String username;

    @JsonIgnore
    @NotNull
    private String password;

    @Email
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String email;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String firstname;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String lastname;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String alias;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private GenderEnum gender;

    private SocialTypeEnum socialType;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
