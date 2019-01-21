import { BasicInterface } from '../basic-interface';
import { ReportConfig } from './report-config';
import { ReportAnalysisColumn } from './report-analysis-column';

export interface ReportAnalysisSheet extends BasicInterface {
    reportConfig: ReportConfig;
    name: string;
    displayName?: string;
    position?: number;
    activeStatus?: boolean;
    sheetProtected?: boolean;

    analysisColumnList: ReportAnalysisColumn[];
}
