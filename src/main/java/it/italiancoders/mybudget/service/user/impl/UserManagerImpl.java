package it.italiancoders.mybudget.service.user.impl;

import it.italiancoders.mybudget.dao.user.UserDao;
import it.italiancoders.mybudget.model.api.RegistrationUser;
import it.italiancoders.mybudget.model.api.SocialTypeEnum;
import it.italiancoders.mybudget.model.api.User;
import it.italiancoders.mybudget.service.user.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserManagerImpl implements UserManager{
    @Autowired
    UserDao userDao;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public User createUser(RegistrationUser registrationUser) {
        User user = User.newBuilder()
                        .alias(registrationUser.getUsername())
                        .username(registrationUser.getUsername())
                        .password(passwordEncoder.encode(registrationUser.getPassword()))
                        .firstname(registrationUser.getFirstname())
                        .lastname(registrationUser.getLastname())
                        .gender(registrationUser.getGender())
                        .socialType(SocialTypeEnum.None)
                        .build();
        userDao.insertUser(user);
        return user;
    }
}
