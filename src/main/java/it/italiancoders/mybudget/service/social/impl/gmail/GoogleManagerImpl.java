package it.italiancoders.mybudget.service.social.impl.gmail;

import it.italiancoders.mybudget.model.api.SocialTypeEnum;
import it.italiancoders.mybudget.model.api.User;
import it.italiancoders.mybudget.service.social.GoogleManager;
import it.italiancoders.mybudget.utils.SocialUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.social.google.api.Google;
import org.springframework.social.google.api.impl.GoogleTemplate;
import org.springframework.social.google.api.plus.Person;
import org.springframework.stereotype.Service;

@Service
public class GoogleManagerImpl implements GoogleManager {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User buildUserFromSocialAccount(String accessToken) {
        Google google = new GoogleTemplate(accessToken);
        Person profile = null;
        try {
            profile = google.plusOperations().getGoogleProfile();
        }catch (Exception e){
            return null;
        }

        return User.newBuilder()
                .username(profile.getId())
                .password(passwordEncoder.encode("*"))
                .email(profile.getEmails() == null || profile.getEmails().isEmpty() ? null :profile.getEmails().get(0))
                .alias(profile.getDisplayName())
                .firstname(profile.getGivenName())
                .lastname(profile.getFamilyName())
                .socialType(SocialTypeEnum.Google)
                .gender(SocialUtils.fromSocialValue(profile.getGender()))
                .build();

    }
}
