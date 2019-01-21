package zw.co.hisolutions.pos.masters.service.impl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import zw.co.hisolutions.pos.masters.entity.PaymentType;
import zw.co.hisolutions.pos.sell.api_controller.SellController; 
import zw.co.hisolutions.pos.sell.entity.dao.PaymentTypeDao;
import zw.co.hisolutions.pos.sell.services.PaymentTypeService;

/**
 *
 * @author dgumbo
 */
@Service
public class PaymentTypeServiceImpl implements PaymentTypeService {

    private final PaymentTypeDao paymentTypeDao;

    public PaymentTypeServiceImpl(PaymentTypeDao paymentTypeDao) {
        this.paymentTypeDao = paymentTypeDao;
    }

    @Override
    public PaymentType update(long id, PaymentType data) throws IllegalArgumentException {
        checkDefaultPaymentType(data) ;
        return PaymentTypeService.super.update(id, data); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public PaymentType save(PaymentType data) throws IllegalArgumentException {
        checkDefaultPaymentType(data) ;
        return PaymentTypeService.super.save(data); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public PaymentType create(PaymentType data) throws IllegalArgumentException {
        checkDefaultPaymentType(data) ;
        return PaymentTypeService.super.create(data); //To change body of generated methods, choose Tools | Templates.
    } 
    
    private void checkDefaultPaymentType(PaymentType paymentType){
        if(paymentType.isDefaultPaymentType()){
            paymentTypeDao.removeDefaultForAllPaymentTypes() ;
        }
    }

    @Override
    public Class getController() {
        return SellController.class;
    }

    @Override
    public JpaRepository<PaymentType, Long> getDao() {
        return paymentTypeDao;
    }
}
