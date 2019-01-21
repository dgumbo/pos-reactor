import { Currency } from 'shared/models/masters/currency';

export interface GlobalExchangeRate {
    currency: Currency;
    currencyRate: number;
    bondRate: number;
}
