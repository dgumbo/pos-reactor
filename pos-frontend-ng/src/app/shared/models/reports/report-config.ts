import { BasicInterface } from '../basic-interface';
import { ReportConfigParameter } from './report-config-parameter';
import { ReportAnalysisSheet } from './report-analysis-sheet';
import { ReportSubCategory } from './report-sub-category';

export interface ReportConfig extends BasicInterface {
    name: string;
    columns: string;
    nativeQuery: string;
    unitQuery?: string;

    reportConfigParameters?: ReportConfigParameter[];
    reportAnalysisSheets?: ReportAnalysisSheet[];

    reportSubCategory?: ReportSubCategory;
}
