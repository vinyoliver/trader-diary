package com.traderdiary.service;

import br.com.caelum.stella.validation.InvalidStateException;
import com.traderdiary.exception.AppException;
import com.traderdiary.model.*;
import com.traderdiary.security.AppContext;
import com.traderdiary.utils.BRUtils;
import com.traderdiary.wrapper.AlteraSenhaWrapper;
import com.traderdiary.wrapper.UsuarioWrapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Stateless
public class UsuarioService extends BaseService<Usuario> {

    @Inject
    private AppContext appContext;

    @Inject
    private EmpresaService empresaService;

    @Transactional(TxType.NOT_SUPPORTED)
    public Usuario findByIdAndUnidade(Long id, Unidade unidade) {
        QUsuario qUsuario = QUsuario.usuario;
        return instanceJPQLQuery().from(qUsuario)
                .where(qUsuario.id.eq(id).and(qUsuario.unidades.contains(unidade))).uniqueResult(qUsuario);
    }

    @Transactional(TxType.NOT_SUPPORTED)
    public Usuario findByCpf(String cpf) {
        QUsuario qUsuario = QUsuario.usuario;
        return instanceJPQLQuery().from(qUsuario).where(qUsuario.cpf.eq(cpf)).uniqueResult(qUsuario);
    }

    @Transactional
    public Usuario criarNovoUsuario(Usuario model) {
        model.setEmpresa(empresaService.findById(Empresa.class, model.getEmpresa().getId()));
        model.setSenha(new BCryptPasswordEncoder().encode(model.getSenha()));
        return save(model);
    }

    @Override
    @Transactional
    public Usuario save(Usuario model) {
        return super.save(model);
    }

    private void checkCpf(Usuario usuario) {
        final Usuario usuarioPorCpf = findByCpf(usuario.getCpf());
        if (usuarioPorCpf != null && !Objects.equals(usuarioPorCpf.getId(), usuario.getId())) {
            throw new AppException("erro.cpf.ja.registrado");
        }

        try {
            BRUtils.validadorDeCPF(usuario.getCpf());
        } catch (InvalidStateException e) {
            throw new AppException("erro.cpf.invalido");
        }
    }

    @Transactional
    public void alterarSenha(AlteraSenhaWrapper dados) {
        Usuario entity = findById(Usuario.class, dados.getId());
        appContext.podeAcessar(entity);//TODO: Tratar a alteração em outros cenarios.
        if (dados.getSenhaAtual() != null &&
                !new BCryptPasswordEncoder().matches(dados.getSenhaAtual(), entity.getSenha())) {
            throw new AppException("erro.rest.exception", "Senha atual inválida");
        }
        if (dados.getSenhaNova().length() < 6 ||
                !dados.getSenhaNova().toUpperCase().matches("^(?=.*[A-Z])(?=.*[0-9])[A-Z0-9]+$")) {
            throw new AppException("erro.formato.senha.invalido");
        }
        entity.setSenha(new BCryptPasswordEncoder().encode(dados.getSenhaNova()));
        save(entity);
    }

    @Transactional(TxType.NOT_SUPPORTED)
    public List<Usuario> findByPerfis(List<Perfil> perfis) {
        QUsuario qUsuario = QUsuario.usuario;
        QPerfil qPerfil = QPerfil.perfil;

        return instanceJPQLQuery().from(qUsuario).join(qUsuario.perfil, qPerfil)
                .where(qUsuario.ativo.isTrue()
                        .and(qPerfil.ativo.isTrue())
                        .and(qUsuario.unidades.contains(appContext.getUnidadeSelecionada()))
                        .and(qPerfil.in(perfis)))
                .list(qUsuario);
    }

    @Transactional(TxType.NOT_SUPPORTED)
    public List<Usuario> findByPerfisEUnidade(List<Perfil> perfis, Unidade unidade) {
        QUsuario qUsuario = QUsuario.usuario;
        QPerfil qPerfil = QPerfil.perfil;

        return instanceJPQLQuery().from(qUsuario).join(qUsuario.perfil, qPerfil)
                .where(qUsuario.ativo.isTrue().and(qPerfil.ativo.isTrue())
                        .and(qUsuario.unidades.contains(unidade)).and(qPerfil.in(perfis)))
                .list(qUsuario);
    }

    @Override
    public void inactive(Usuario model) {
        if (appContext.getUsuarioLogado().getId().equals(model.getId())) {
            throw new AppException("erro.desativacao.usuario.atual");
        }
        super.inactive(model);
    }

    public List<UsuarioWrapper> buscarTodosOsUsuariosDaEmpresaAtual() {
        QUsuario qUsuario = QUsuario.usuario;
        Empresa empresaAtual = appContext.getEmpresaSelecionada();

        List<Usuario> usuariosDaEmpresa = instanceJPQLQuery()
                .from(qUsuario)
                .where(qUsuario.empresa.id.eq(empresaAtual.getId()).and(qUsuario.ativo.isTrue()))
                .orderBy(qUsuario.nome.asc())
                .list(qUsuario);

        return usuariosDaEmpresa.stream().map(usuario -> new UsuarioWrapper(usuario)).collect(Collectors.toList());
    }
}
