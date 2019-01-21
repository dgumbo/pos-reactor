/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zw.co.hisolutions.pos.common.models;

/**
 *
 * @author com4t
 */
public enum OutputType {
    //warehouse outputs
    ADJUSTMENT_ISSUE,
    DEPARTMENT_ISSUE,
    PURCHASEORDER,
    STOCKREQUEST, 
    BOOK_STOCK_SHEET, 
    BOOK_STOCK_SHEET_WAREHOUSE,
    STOCK_COUNT_SHEET, 
    STOCK_VALUATION_REPORT, 
    STOCK_UPDATE,
    VARIANCE_REPORT,
    STOCK_RECEIVE,
    ADJUSTMENT_RECEIPT,
    PURCHASE_RECEIPT,
    CASH_PURCHASE,
    STOCK_ISSUE,
    GOODS_RECEIVED_VOUCHER,
    COST_ADJUSTMENT,
    REQUISITION,
    
    //bill outputs
    DEPOSIT_ALLOCATION,
    INSTITUTION_INVOICE,
    INSTITUTION_DEPOSIT,
    MEDICAL_CLAIM_FORM,
    REFUND,
    RECEIPT,
    PSPCLAIMFORM,
    DAYEND,
    DEPOSIT,
    PATIENT_INVOICE,
    
    //edi outputs
    MEDICAL_CLAIM_EDI,
    PSP_CLAIM_EDI,
    CLAIMS_BATCH,
    
    //psp outputs
    FINAL_RECEIPT,
    TRAILER,
    LABEL,
    LABEL_PAGE,
    QUOTATION,
    PRESCRIPTION,
    
    //emr outputs
    REFERRAL_NOTES,
    MEDICAL_CERTIFICATES,
    SERVICE_REQUEST,
    RADIOLOGY_SERVICE_REQUEST,
    LABORATORY_SERVICE_REQUEST,  
    DOCTOR_LETTER,
    
    //patient
    APPOINTMENT, 
    QUEUE_NUMBER,
    JOB_ORDER,

    //Dental
    DENTAL_JOB_CARD,

    //Opto
    OPTOM_PRESCRIPTION,
    OPTOM_JOB_ORDER, 
}
