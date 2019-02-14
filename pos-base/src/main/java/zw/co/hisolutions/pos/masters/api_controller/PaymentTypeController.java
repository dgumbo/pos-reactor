package zw.co.hisolutions.pos.masters.api_controller;
 
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import zw.co.hisolutions.pos.common.controllers.rest.BasicRestController;
import zw.co.hisolutions.pos.common.service.GenericService; 
import zw.co.hisolutions.pos.masters.entity.PaymentType;
import zw.co.hisolutions.pos.sell.services.PaymentTypeService;

/**
 *
 * @author dgumbo
 */
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@Controller
@RequestMapping("/api/masters/payment-type")
public class PaymentTypeController extends BasicRestController<PaymentType, Long> {

    private final PaymentTypeService paymentTypeService;

    public PaymentTypeController(PaymentTypeService paymentTypeService) {
        this.paymentTypeService = paymentTypeService;
    } 
    
    @Override
    public GenericService getService() {
        return this.paymentTypeService;
    }

    @Override
    public Class getControllerClass() {
        return this.getClass();
    }

}
