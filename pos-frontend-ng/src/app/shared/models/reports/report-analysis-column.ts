import { ReportAnalysisSheet } from './report-analysis-sheet';
import { BasicInterface } from '../basic-interface';


export interface ReportAnalysisColumn extends BasicInterface {
    reportAnalysisSheet: ReportAnalysisSheet;
    name: string;
    analysisSector: string;
    displayName?: string;
    position?: number;
    filterValue?: string;
    showSubTotal?: boolean;
    activeStatus?: boolean;
    numberFormat?: string;
    aggregateFunction?: string;
}
