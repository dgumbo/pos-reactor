package zw.co.hisolutions.pos.common.service;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author dgumbo
 * @param <T>
 * @param <B>
 */
public interface BasicListService<T, B> {

    public JpaRepository<T, Long> getDao();

//    @Transactional(isolation = Isolation.READ_UNCOMMITTED, propagation = Propagation.SUPPORTS, readOnly = true)
//    default T getByID(Long id) {
//        return getDao().findOne(id);
//    }

    @Transactional(isolation = Isolation.READ_UNCOMMITTED, propagation = Propagation.REQUIRED)
    T save(T t, List<B> list);

    @Transactional(isolation = Isolation.READ_UNCOMMITTED, propagation = Propagation.SUPPORTS, readOnly = true)
    default List<T> findAll() {
        System.out.println("zw.co.hisolutions.common.basic.service.BasicListService.findAll()");
        return getDao().findAll();
    }
}
