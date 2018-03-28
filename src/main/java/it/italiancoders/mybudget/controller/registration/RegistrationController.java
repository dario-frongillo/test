package it.italiancoders.mybudget.controller.registration;

import it.italiancoders.mybudget.model.api.JwtAuthenticationRequest;
import it.italiancoders.mybudget.model.api.RegistrationUser;
import it.italiancoders.mybudget.model.api.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Locale;

@RestController
public class RegistrationController {

    @Autowired
    @Qualifier("errorMessageSource")
    MessageSource errorMessageSource;


    @RequestMapping(value = "public/v1/register", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody @Valid RegistrationUser user){
        Locale locale = Locale.ITALIAN;
        String s = errorMessageSource.getMessage("javax.validation.constraints.Past.message", null,locale);
        return ResponseEntity.ok(s);
    }
}
