package com.nabenik.cfp.controller;

import javax.enterprise.context.RequestScoped;

@RequestScoped
public class RandomStringGenerator implements RandomStringService {
    
    @Override
    public String generateRandomString(){
        return "Hola soy victor";
    }
}