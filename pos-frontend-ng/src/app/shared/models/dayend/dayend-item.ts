import { BasicInterface } from '../basic-interface';
import { PaymentType } from 'app/masters/models/payment-type';
import { Currency } from '../masters/currency';

export interface DayendItem extends BasicInterface {
    paymentType: PaymentType;
    currency: Currency;
    amount: number;
}
