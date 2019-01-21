import { ReceiptItem } from './receipt-item';
import { BasicInterface } from '../basic-interface';

export interface Receipt extends BasicInterface {
    tenderedAmount: number;
    paidAmount: number;
    payableAmount: number;
    changeAmount: number;
    receiptDate: Date;
    receiptDateTime: Date;

    receiptItems: ReceiptItem[];
}
