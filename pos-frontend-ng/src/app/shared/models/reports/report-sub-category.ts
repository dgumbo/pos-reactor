import {BasicInterface} from "../basic-interface"; 
import {ReportCategory} from "./report-category";
import {ReportConfig} from "./report-config";

export interface ReportSubCategory extends BasicInterface{
    name:string; 
    
    reportConfig ?:ReportConfig ; 
    
    reportCategory?:ReportCategory ;
}