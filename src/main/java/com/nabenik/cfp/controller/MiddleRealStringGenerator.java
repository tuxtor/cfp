/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nabenik.cfp.controller;

import java.math.BigInteger;
import java.security.SecureRandom;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Alternative;

/**
 *
 * @author tuxtor
 */
@Alternative
@RequestScoped
public class MiddleRealStringGenerator implements RandomStringService {
    
    private SecureRandom random = new SecureRandom();
    private int executions;
    
    @Override
    public String generateRandomString() {
        return "Cliente 3 ".concat(new BigInteger(130, random).toString(32)).concat(" ".concat(String.valueOf(executions++)));
    }
    
}
