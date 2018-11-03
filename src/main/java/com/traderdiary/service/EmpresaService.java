package com.traderdiary.service;

import com.traderdiary.model.Empresa;

import javax.ejb.Stateless;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

@Stateless
public class EmpresaService extends BaseService<Empresa> {

    @Transactional(TxType.NOT_SUPPORTED)
    public Empresa findById(Long id) {
        return findById(Empresa.class, id);
    }

    @Override
    public Empresa save(Empresa model) {
        if (!model.isNovo()) {
            Empresa empresaDB = findById(model.getId());
//			if (empresaDB.getImagem() != null && !empresaDB.getImagem().equals(model.getImagem())) {
//				FileUtils.deleteFile(empresaDB.getImagem().getPath());
//			}
        }

        return super.save(model);
    }
}
