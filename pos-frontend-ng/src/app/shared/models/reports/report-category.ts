import { BasicInterface } from '../basic-interface';
import { ReportSubCategory } from './report-sub-category';

export interface ReportCategory extends BasicInterface {
    name: string;

    reportSubCategoryList?: ReportSubCategory[];
}
