package com.nabenik.cfp.controller;

import java.math.BigInteger;
import java.security.SecureRandom;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Alternative;

@Alternative
@RequestScoped
public class RealRandomStringGenerator implements RandomStringService {

    private SecureRandom random = new SecureRandom();
    
    @Override
    public String generateRandomString() {
        return new BigInteger(130, random).toString(32);
    }
}