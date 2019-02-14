export interface StockReOrderView {
    productCategory: string;
    stockItem: string;
    requiredQuantity: number;
    unitCost: number;
    totalCost: number;
    currentStock: number;
    averageDailyConsumption: number;
    currentStockDepletionDays: number;
    minOrderQuantity: number;
    totalSafetyStock: number;
}
