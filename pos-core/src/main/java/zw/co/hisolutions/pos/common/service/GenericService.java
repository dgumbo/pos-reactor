package zw.co.hisolutions.pos.common.service;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import zw.co.hisolutions.pos.common.entity.BaseEntity;

/**
 * @author denzil
 * @param <T>
 * @param <ID>
 */
public interface GenericService<T extends BaseEntity, ID extends Serializable> {

    @Transactional(isolation = Isolation.READ_UNCOMMITTED, propagation = Propagation.SUPPORTS, readOnly = true)
    default List<T> findAll() {
        return getDao().findAll();
    }

    @Transactional(isolation = Isolation.READ_UNCOMMITTED, propagation = Propagation.SUPPORTS, readOnly = true)
    default T find(ID id) {
        Optional<T> optT = getDao().findById(id);
        T entity = optT.isPresent() ? optT.get() : null;
        // T entity = getDao().getOne(id); // Causes error : Could not write content: could not initialize proxy - no Session

        return entity;
    }

    @Transactional(isolation = Isolation.READ_UNCOMMITTED, propagation = Propagation.REQUIRED)
    default T create(T data) throws IllegalArgumentException {
        T entity = getDao().save(data);
        return entity;
    }

    @Transactional(isolation = Isolation.READ_UNCOMMITTED, propagation = Propagation.REQUIRED)
    default T save(T data) throws IllegalArgumentException {
        T entity = getDao().save(data);
        return entity;
    }

    default void delete(ID id) throws IllegalArgumentException {
        T entity = find(id);
        getDao().delete(entity);
    }

    default T update(long id, T data) throws IllegalArgumentException {
        data.setId(id);
        T entity = getDao().save(data);
        return entity;
    }

    public JpaRepository<T, ID> getDao();
    public Class getController();
}
