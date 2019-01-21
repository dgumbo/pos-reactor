import {Product} from './product';
import {StockSupplier} from './supplier';

export interface StockReceiveItem {
  product: Product;
  supplier: StockSupplier;
  unitOfMeasure: string;
  quantity: number;
  unitCost: number;
  batchNumber: string;
  totalCost?: number;
  sellingPrice?: number;
  barcode?: string;
}
