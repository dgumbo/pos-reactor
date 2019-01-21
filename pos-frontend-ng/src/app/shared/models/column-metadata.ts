import { BasicInterface } from './basic-interface';

export interface ColumnMetadata extends BasicInterface {
    position: string;
    name: string;
    title: string;
    type?: string;
}
