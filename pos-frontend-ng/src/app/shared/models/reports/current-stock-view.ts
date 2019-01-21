export interface CurrentStockView {
    productCategory: string;
    stockItem: string;
    quantity: number;
    lrcr: number;
    wac: number;
    lrcrTotalValue: number;
    wacTotalValue: number;
    sellingPrice: number;
    totalSellingValue: number;
    currency: string;
    batchNumber: string;
    expiryDate: Date;
    stockStatus: any;
    consignment: string;
}
