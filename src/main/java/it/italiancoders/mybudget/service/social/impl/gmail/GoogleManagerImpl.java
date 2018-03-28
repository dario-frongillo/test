package it.italiancoders.mybudget.service.social.impl.gmail;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.json.jackson.JacksonFactory;
import it.italiancoders.mybudget.model.api.SocialTypeEnum;
import it.italiancoders.mybudget.model.api.User;
import it.italiancoders.mybudget.service.social.GoogleManager;
import it.italiancoders.mybudget.utils.SocialUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.social.connect.Connection;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.social.google.api.Google;
import org.springframework.social.google.api.impl.GoogleTemplate;
import org.springframework.social.google.api.plus.Person;
import org.springframework.social.google.connect.GoogleConnectionFactory;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.OAuth2Operations;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
            //return null;
        }

          List mClientIDs;
          String mAudience;
          GoogleIdTokenVerifier mVerifier;
          JsonFactory mJFactory;
         String mProblem = "Verification failed. (Time-out?)";
        mClientIDs = Arrays.asList(Arrays.asList("1079647101570-pech7iq2v03fg1cl31molh6rr9740166.apps.googleusercontent.com"));
        mAudience = null;
        NetHttpTransport transport = new NetHttpTransport();
        mJFactory = new GsonFactory();
        mVerifier = new GoogleIdTokenVerifier(transport, mJFactory, "1079647101570-pech7iq2v03fg1cl31molh6rr9740166.apps.googleusercontent.com");
                //"1079647101570-8l2adk4hsop64bbbtiditmbbmdcvd481.apps.googleusercontent.com");
        GoogleIdToken.Payload payload = null;
        try {
            GoogleIdToken token = GoogleIdToken.parse(mJFactory, "eyJhbGciOiJSUzI1NiIsImtpZCI6IjM3NmVhMWUyZjRjOTM3YzMzM2QxZTI0YjU2NDczOGZjMDRjOTkwMDkifQ.eyJhenAiOiIxMDc5NjQ3MTAxNTcwLXBlY2g3aXEydjAzZmcxY2wzMW1vbGg2cnI5NzQwMTY2LmFwcHMuZ29vZ2xldXNlcmNvbnRlbnQuY29tIiwiYXVkIjoiMTA3OTY0NzEwMTU3MC04bDJhZGs0aHNvcDY0YmJidGlkaXRtYmJtZGN2ZDQ4MS5hcHBzLmdvb2dsZXVzZXJjb250ZW50LmNvbSIsInN1YiI6IjEwMzI0Mjk0MjI2MTY0OTk5NDkyNSIsImVtYWlsIjoiZmF0dGF6em84MkBnbWFpbC5jb20iLCJlbWFpbF92ZXJpZmllZCI6dHJ1ZSwiZXhwIjoxNTIyMjc3MDg1LCJpc3MiOiJodHRwczovL2FjY291bnRzLmdvb2dsZS5jb20iLCJpYXQiOjE1MjIyNzM0ODUsIm5hbWUiOiJHaWFubHVjYSBGYXR0YXJzaSIsInBpY3R1cmUiOiJodHRwczovL2xoNC5nb29nbGV1c2VyY29udGVudC5jb20vLWM3RjQzajFvLUxzL0FBQUFBQUFBQUFJL0FBQUFBQUFBQ2VjL1RyLVRseFJyTFE4L3M5Ni1jL3Bob3RvLmpwZyIsImdpdmVuX25hbWUiOiJHaWFubHVjYSIsImZhbWlseV9uYW1lIjoiRmF0dGFyc2kiLCJsb2NhbGUiOiJpdCJ9.U7O_MrNe6W8Krp5VboLqg2Etfi2Tc9s3KsJjPSqpNiXDGS0IwULEG9VgMihxICgQXEBjIQYfyREaeU52JeSblPBuNn7w-JqJ6uMwbenRNuV_f7_rxTxWy-FSm71MYd2N-EmD-xaE0VleCZqPnRhPaG-x0nzxzliZeToBYzjaNRngSnKeBpCwtDJT15ddpnJuQjR12zrUOHtUWaNcmnvaWlQOqinxAHkHbqmk8HpIBvt5OTBVcXLpc6f1KsRPehyIRApuNylkS4Cxjs-KhsXV6Ga1-BG0VRNgmxWeel677QUk4TV_BDEyo_N1ObT1dxO8AFrWTAomx--sP68bv-w8yw");
           GoogleIdToken.Payload payloadq = token.getPayload();
            payloadq.get("email")
            if (mVerifier.verify(token)) {
                GoogleIdToken.Payload tempPayload = token.getPayload();
                if (!tempPayload.getAudience().equals(mAudience))
                    mProblem = "Audience mismatch";
                else
                    payload = tempPayload;
            }
        } catch (GeneralSecurityException e) {
            mProblem = "Security issue: " + e.getLocalizedMessage();
        } catch (IOException e) {
            mProblem = "Network problem: " + e.getLocalizedMessage();
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
