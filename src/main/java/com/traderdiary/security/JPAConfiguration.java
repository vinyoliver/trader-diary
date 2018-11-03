package com.traderdiary.security;

import org.springframework.context.annotation.Bean;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManagerFactory;

public class JPAConfiguration {

    @Bean
    public EntityManagerFactory emf() throws NamingException {
        Context ctx = new InitialContext();
        EntityManagerFactory lookup = (EntityManagerFactory) ctx
                .lookup("java:jboss/appEntityManagerFactory");
        return lookup;
    }
}
