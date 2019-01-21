import { SupplierClassification } from './enums/supplier-classification';

export interface StockSupplier {
    id?: number;
    name: string;
    address: string;
    classification: SupplierClassification;
}
