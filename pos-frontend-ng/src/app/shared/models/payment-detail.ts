import { Currency } from './masters/currency';
import { ReceiptItem } from './sell/receipt-item';

export interface PaymentDetail {
    payableAmount: number;
    tenderedAmount: number;
    changeAmount?: number;

    receiptItems: ReceiptItem[];
}
