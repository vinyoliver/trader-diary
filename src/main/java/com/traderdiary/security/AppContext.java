package com.traderdiary.security;

import com.traderdiary.model.Empresa;
import com.traderdiary.model.Unidade;
import com.traderdiary.model.Usuario;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.io.Serializable;

@Named
@SessionScoped
public class AppContext implements Serializable {

	private Unidade unidadeSelecionada;
	
	public Unidade getUnidadeSelecionada() {
		return unidadeSelecionada;
	}

	public Empresa getEmpresaSelecionada() {
        throw new UnsupportedOperationException("Not ready yet...");
	}
	
	public void setUnidadeSelecionada(Unidade unidadeSelecionada) {
		this.unidadeSelecionada = unidadeSelecionada;
	}
	
	public Usuario getUsuarioLogado() {
		return (Usuario)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}

	public void podeAcessar(Usuario usuario) {
		if (!getUsuarioLogado().equals(usuario)) {
			throw new WebApplicationException(Response.Status.FORBIDDEN);
		}
	}
}
