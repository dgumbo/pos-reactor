import { BasicInterface } from '../basic-interface';

export interface Currency extends BasicInterface {
    name: String;

    symbol?: String;
    currencyRate: number;
    bondRate: number;

    defaultCurrency: boolean;
    displayOnSummary: boolean;
}
