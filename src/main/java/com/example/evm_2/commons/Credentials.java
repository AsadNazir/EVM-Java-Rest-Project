package com.example.evm_2.commons;

import com.auth0.jwt.algorithms.Algorithm;

public abstract class Credentials {

    public static final Algorithm SECRET_KEY_JWT = Algorithm.HMAC256("ILOVEJAVA");
    public static final String EMAIL_PWD ="";
}
