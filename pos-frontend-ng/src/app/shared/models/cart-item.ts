import { Product } from './product';
import { CurrentStock } from './current-stock';

export interface CartItem {
  product: Product;
  batchNumber: string;
  quantity: number;
  unit_price: number;

  stock: CurrentStock;
  discount?: number;

  stock_adjust?: boolean;

}
