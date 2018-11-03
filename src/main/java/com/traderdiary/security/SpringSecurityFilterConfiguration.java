package com.traderdiary.security;

import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

public class SpringSecurityFilterConfiguration extends
        AbstractSecurityWebApplicationInitializer {

    public SpringSecurityFilterConfiguration() {
        super(SecurityConfiguration.class, SystemUserDAO.class, JPAConfiguration.class, CustomSuccessHandler.class);
    }
}
