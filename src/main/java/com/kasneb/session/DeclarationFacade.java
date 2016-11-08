package com.kasneb.session;

import com.kasneb.entity.Declaration;
import com.kasneb.entity.Student;
import com.kasneb.entity.StudentDeclaration;
import com.kasneb.exception.CustomHttpException;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

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

    public void update(StudentDeclaration entity) throws CustomHttpException {
        this.getEntityManager().merge((Object) entity);
        this.getEntityManager().flush();
    }

    public List<Student> findByResponse(Integer declarationId, Boolean response) {
        TypedQuery<Student> query = em.createQuery("SELECT s FROM Student s JOIN s.studentCourses sc JOIN sc.studentCourseDeclarations d WHERE d.studentDeclarationPK.declarationId =:declarationId AND d.response =:response", Student.class);
        query.setParameter("declarationId", declarationId);
        query.setParameter("response", response);
        return query.getResultList();
    }
}
