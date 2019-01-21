import { BasicInterface } from '../basic-interface';
import { ReportConfig } from './report-config';

export interface ReportConfigParameter extends BasicInterface {
    reportConfig: ReportConfig;
    parameter: string;
    name: string;
    columnName: string;
    parameterType: string;
    selectSql?: string;
    selectValueField?: string;
    optional?: boolean;

    dropDownSelectOptions?: string[];
}
