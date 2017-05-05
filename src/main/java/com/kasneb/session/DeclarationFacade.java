package com.kasneb.session;

import com.kasneb.entity.Declaration;
import com.kasneb.entity.StudentDeclaration;
import com.kasneb.exception.CustomHttpException;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class DeclarationFacade
        extends AbstractFacade<Declaration> {

    @PersistenceContext(unitName = "com.kasneb_kasneb_new_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return this.em;
    }

    public DeclarationFacade() {
        super((Class) Declaration.class);
    }

    @Override
    public List<Declaration> findAll() {
        return super.findAll();
    }

    public void update(StudentDeclaration entity) throws CustomHttpException {
        this.getEntityManager().merge((Object) entity);
        this.getEntityManager().flush();
    }

}
