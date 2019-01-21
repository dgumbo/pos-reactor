import { CartItem } from './cart-item';
import { PaymentDetail } from './payment-detail';

export interface Cart {
  id?: number;
  items: CartItem[];
  itemsCount: number;
  totalAmount: number;
  totalEcocashAmount?: number;
  payment: PaymentDetail;

  dateSaleDate?: Date;
  dateSale?: boolean;
}
