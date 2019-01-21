import { PaymentType } from 'app/masters/models/payment-type';
import { Currency } from '../masters/currency';
import { BasicInterface } from '../basic-interface';

export interface ReceiptItem extends BasicInterface {
    currency: Currency;
    paymentType: PaymentType;
    amount: number;
    currencyRate: number;
    convertedAmount: number;

    remarks?: String;
}
