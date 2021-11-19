package cn.mdmbct.seckill.app.common.query;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

/**
 * @author mdmbct  mdmbct@outlook.com
 * @date 2021/11/19 9:45
 * @modified mdmbct
 * @since 0.1
 */
@Slf4j
@Repository
public class JpaDynamicQuery implements DynamicQuery {

    @PersistenceContext
    private EntityManager em;

    public EntityManager getEntityManager() {
        return em;
    }

    @Override
    public void save(Object entity) {
        em.persist(entity);
    }

    @Override
    public void update(Object entity) {
        em.merge(entity);
    }

    @Override
    public <T> void delete(Class<T> entityClass, Object entityid) {
        delete(entityClass, new Object[]{entityid});
    }

    @Override
    public <T> void delete(Class<T> entityClass, Object[] entityids) {
        for (Object id : entityids) {
            em.remove(em.getReference(entityClass, id));
        }
    }

    private Query createNativeQuery(String sql, Object... params) {
        Query q = em.createNativeQuery(sql);
        if (params != null && params.length > 0) {
            for (int i = 0; i < params.length; i++) {
                // 与Hiberante不同,jpa
                q.setParameter(i + 1, params[i]);
                // query从位置1开始
            }
        }
        return q;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> List<T> nativeQueryList(String nativeSql, Object... params) {
        Query q = createNativeQuery(nativeSql, params);
        q.unwrap(SQLQuery.class).setResultTransformer(Transformers.TO_LIST);
        return q.getResultList();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> List<T> nativeQueryListModel(Class<T> resultClass,
                                            String nativeSql, Object... params) {
        Query q = createNativeQuery(nativeSql, params);
        ;
        q.unwrap(SQLQuery.class).setResultTransformer(Transformers.aliasToBean(resultClass));
        return q.getResultList();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> List<T> nativeQueryListMap(String nativeSql, Object... params) {
        Query q = createNativeQuery(nativeSql, params);
        q.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return q.getResultList();
    }

    @Override
    public Object nativeQueryObject(String nativeSql, Object... params) {
        return createNativeQuery(nativeSql, params).getSingleResult();
    }

    @Override
    public int nativeExecuteUpdate(String nativeSql, Object... params) {
        return createNativeQuery(nativeSql, params).executeUpdate();
    }

    @Override
    public Object[] nativeQueryArray(String nativeSql, Object... params) {
        return (Object[]) createNativeQuery(nativeSql, params).getSingleResult();
    }
}
