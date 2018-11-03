package com.traderdiary.security;

import com.traderdiary.model.Usuario;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.enterprise.inject.Model;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Model
public class AdminSystemUserBean {

    private Usuario systemUser = new Usuario();

    @PersistenceContext
    private EntityManager entityManager;
    private String rawPassword;


    public String getRawPassword() {
        return rawPassword;
    }

    public void setRawPassword(String rawPassword) {
        this.rawPassword = rawPassword;
    }

    public Usuario getSystemUser() {
        return systemUser;
    }

    public void setSystemUser(Usuario systemUser) {
        this.systemUser = systemUser;
    }

    @Transactional
    public void save() {
        systemUser.setSenha(new BCryptPasswordEncoder().encode(rawPassword));
        entityManager.persist(systemUser);
    }

}
