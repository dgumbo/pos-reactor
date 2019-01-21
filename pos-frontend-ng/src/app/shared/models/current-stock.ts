import { StockStatus } from './enums/stock-status';
import { BasicInterface } from './basic-interface';

export interface CurrentStock extends BasicInterface {
    stockItemId: number;
    quantity: number;
    unitCost: number;
    batchNumber: string;
    consignment: boolean;
    cashOnly: boolean;
    stockStatus: StockStatus;
    expiryDate?: Date;
    barcode?: string;
    vat?: number;
}
