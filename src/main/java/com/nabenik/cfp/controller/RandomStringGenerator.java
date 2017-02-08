package com.nabenik.cfp.controller;

import javax.enterprise.context.RequestScoped;

@RequestScoped
public class RandomStringGenerator implements RandomStringService {
    
    @Override
    public String generateRandomString(){
        return "The not so random String";
    }
}