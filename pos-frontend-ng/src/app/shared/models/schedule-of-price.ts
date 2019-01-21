import { ChargeType } from './enums/charge-type';
import { Currency } from './masters/currency';

export interface ScheduleOfPrice {
    id?: number;
    chargeType: ChargeType;
    currency: Currency;
    description?: string;
    price: number;
    ratio: number;
    additionalCharge: number;
}
