import { SupplierClassification } from './enums/supplier-classification';
import { Product } from './product';

export interface SellItem {
    id?: number;
    product: Product;
    cashOnly: boolean;
    quantity: number;
    unitCost: number;
    vat: number;
    totalCost: number;
    vatPercentage: number;

    //   stockTransactionLine :StockTransactionLine ;
}
