import { BasicInterface } from '../basic-interface';
import { Product } from '../product';
import { CurrentStock } from '../current-stock';

export interface StockTakeLine extends BasicInterface {
  comment: string;
  stockItem: Product;
  quantity: number;
  currentStock: CurrentStock;
  batchNumber: string;
}
