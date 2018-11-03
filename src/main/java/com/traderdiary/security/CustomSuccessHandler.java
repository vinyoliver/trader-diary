package com.traderdiary.security;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.traderdiary.model.Autoridade;
import com.traderdiary.model.Usuario;
import com.traderdiary.utils.JPAUtils;
import com.traderdiary.utils.JSONUtils;
import com.traderdiary.wrapper.UsuarioLogadoWrapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Component
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws
            IOException, ServletException {

        List<Autoridade> autoridades =
                (List<Autoridade>) SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        final String cpf = SecurityContextHolder.getContext().getAuthentication().getName();
        final TypedQuery<Usuario> query = em.createNamedQuery("findByCpf", Usuario.class).setParameter("cpf", cpf);
        final Usuario usuario = JPAUtils.uniqueResultOrNull(query);
        final UsuarioLogadoWrapper usuarioLogadoWrapper = new UsuarioLogadoWrapper(usuario, autoridades);

        ObjectNode objectNode = JSONUtils.objectNode(usuarioLogadoWrapper);
        response.getWriter().write(objectNode.toString());
    }

}