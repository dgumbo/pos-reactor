import { TransactionStatus } from './enums/transaction-status';
import { SellItem } from './sell-item';
import { Receipt } from './sell/receipt';
import { BasicInterface } from './basic-interface';

export interface Sell extends BasicInterface {
    transactionStatus: TransactionStatus;
    sellAmount: number;
    itemCount: number;
    sellItems: SellItem[];
    sellDate: Date;
    sellDateTime: Date;
    username: String;

    receipt: Receipt;

    // stockTransactionType: StockTransactionType;
}
