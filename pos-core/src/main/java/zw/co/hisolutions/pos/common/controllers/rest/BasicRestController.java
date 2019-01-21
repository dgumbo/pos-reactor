package zw.co.hisolutions.pos.common.controllers.rest;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;  
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;   
import zw.co.hisolutions.pos.common.entity.BaseEntity;
import zw.co.hisolutions.pos.common.service.GenericService;
import zw.co.hisolutions.pos.common.util.Results;
import zw.co.hisolutions.pos.common.util.Results.DBActionResult;

/**
 * Base controller.
 *
 * @author denzil
 * @param <T>
 */
 
public abstract class BasicRestController<T extends BaseEntity, ID extends Serializable> {

    public abstract GenericService getService();
    public abstract Class getControllerClass(); 
  //  private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(getControllerClass());

    @GetMapping(value = "", produces = {MediaType.APPLICATION_JSON_VALUE, "application/hal+json"})
    public ResponseEntity<List<T>> getAll() {
        List<T> entityList = getService().findAll();

        return new ResponseEntity<>(entityList, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, "application/hal+json"})
    public ResponseEntity<T>getResource (@PathVariable ID id) {
        T entity = (T) getService().find(id);
        //System.out.println("\n T B4 Save : " + "entity" + "\n" + "Entity class : " + entity.getClass().getName() + "\n enity is null : " + (entity == null));
        return new ResponseEntity<>(entity, HttpStatus.OK);
    }

    @GetMapping(value = "/get/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, "application/hal+json"})
    public ResponseEntity<?> getEntity (@PathVariable ID id) {
        T entity = (T) getService().find(id);
        //Resource resource = getService().buildResource(entity);
        //System.out.println("\n T B4 Save : " + resource.getContent() + "\n");
        return new ResponseEntity<>(entity, HttpStatus.OK);
    }

    @PostMapping(value = "", produces = {MediaType.APPLICATION_JSON_VALUE, "application/hal+json"})
    public ResponseEntity<T> create(@RequestBody T resourceEntity) throws Exception {
        System.out.println("\n"+ resourceEntity.getClass() .getName() + "  B4 Save : " + resourceEntity + "\n");

        ResponseEntity responseEntity;
        HttpStatus httpStatus;
        try {
            T entity = (T) getService().create( resourceEntity);
            httpStatus = HttpStatus.CREATED;
            responseEntity = new ResponseEntity<>(entity, httpStatus);
        } catch (Exception ex) {
            httpStatus = HttpStatus.NOT_IMPLEMENTED;
            responseEntity = new ResponseEntity<>(new Results(DBActionResult.EncounteredError, "Could not create entity.", "new", this.getClass()), httpStatus);

            //System.out.println(ex.getMessage());
            Logger.getLogger(getControllerClass().getName()).log(Level.SEVERE, null, ex);
        }

        return responseEntity;
    }

    @PutMapping(value = "/{id}",  produces = {MediaType.APPLICATION_JSON_VALUE, "application/hal+json"})
    public ResponseEntity<T> edit(@PathVariable Long id, @RequestBody T entity) {
        //System.out.println("\nzw.co.hisolutions.tutorials.rest.controllers.T Controller.editT ()\n");

        ResponseEntity responseEntity;
        HttpStatus httpStatus;
        try {
            entity = (T) getService().update(id, entity);
            httpStatus = HttpStatus.OK;
            responseEntity = new ResponseEntity<>(entity, httpStatus);
        } catch (Exception ex) {
            httpStatus = HttpStatus.NOT_MODIFIED;
            responseEntity = new ResponseEntity<>(new Results(DBActionResult.EncounteredError, "Could not edit entity.", id.toString(), this.getClass()), httpStatus);
            Logger.getLogger(getControllerClass().getName()).log(Level.SEVERE, null, ex);
        }

        return responseEntity;
    }

    @DeleteMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, "application/hal+json"})
    public ResponseEntity<Results> delete(@PathVariable ID id) {
        //   System.out.println("\nzw.co.hisolutions.tutorials.rest.controllers.T Controller.deleteT ()\n");

        Results results;
        HttpStatus httpStatus;
        try {
            getService().delete(id);
            results = new Results(DBActionResult.Success, "Successiful Deleted", id.toString(), this.getClass());
            httpStatus = HttpStatus.OK;
        } catch (Exception ex) {
            httpStatus = HttpStatus.NOT_IMPLEMENTED;
            results = new Results(DBActionResult.Failed, "Failed to Delete", id.toString(), this.getClass());
            Logger.getLogger(getControllerClass().getName() ).log(Level.SEVERE, null, ex);
        }

        return new ResponseEntity<>(results, httpStatus);
    }
}
