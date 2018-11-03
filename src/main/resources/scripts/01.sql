--Função para inserção de perfil/autoridades
CREATE OR REPLACE FUNCTION public.inserir_autoridade_diversos_papeis(nome_perfis _varchar, nome_autoridades _varchar)
  RETURNS int4
AS
$BODY$
DECLARE
  tamanhoArrayPerfis INTEGER;
  tamanhoArrayAutoridades INTEGER;
BEGIN
  tamanhoArrayPerfis := array_length($1, 1);
  tamanhoArrayAutoridades := array_length($2, 1);
  FOR i in 1 .. tamanhoArrayPerfis LOOP
    FOR j in 1 .. tamanhoArrayAutoridades LOOP
      INSERT INTO permissao (perfil_id, autoridade)
        SELECT p.id, nome_autoridades[j] FROM perfil p WHERE p.nome = trim(both from nome_perfis[i]);
    END LOOP;
  END LOOP;
  RETURN tamanhoArrayPerfis;
END;
$BODY$
LANGUAGE plpgsql VOLATILE;


insert into perfil(nome, ativo, visivel, descricao) values ('Administrador Sistema', true, false, 'x');
insert into perfil(nome, ativo, visivel, descricao) values ('usuario', true, true, 'y');

--Permissao de acesso
SELECT
  inserir_autoridade_diversos_papeis('{Administrador Sistema}',
                                     '{ROLE_EDITAR_OPERACAO, ROLE_CONSULTAR_OPERACAO, ROLE_CONSULTAR_USUARIO, ROLE_EDITAR_USUARIO, ROLE_SYSTEM_ADMIN}');


insert into usuario (nome, sobrenome, ativo, data_cadastro, email, senha, perfil_id) values ('Admin', 'Admin', true, current_date, 'admin@admin.com', '$2a$10$3TgC8wmCy6Q0t3bPB92yf.UkYeplKv/iut0yP0y7Nn6Mdy7StHkbq', 1);
insert into unidade (codigo, nome, ativo, data_cadastro, usuario_id) values(1, 'Padrão', true, current_date, (select max(id) from usuario));