/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zw.co.hisolutions.pos.client.printer.model;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.Data;
            
/**
 *
 * @author com4t
 */
@XmlRootElement
@Data
public class PrintMsg {
    private PrintMsgRequestType requestType;
    private List<String> printers=new ArrayList<>();
    private String document;
    private String jsonData;
    private String filename;
    private boolean successStatus=false;
    private String selectedPrinter;
}
