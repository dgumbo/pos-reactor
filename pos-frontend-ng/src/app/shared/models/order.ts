import { Product } from './product';
import { Customer } from './customer';
import { CartItem } from './cart-item';

export interface Order {
  orderNumber: string;
  products?: Product[];
  cartTotal: number;
  cartNumItems?: number;


  id: number;
  sub_total: number;
  total: number;
  amount_paid: number;
  is_draft: boolean;
  invoice_number: number;
  reference_number: number;
  discount_value: number;
  created_on: Date;

  doctor: string;
  customer_id: number;
  user_id: number;
  address_id: number;
  store_id: number;
  current_status_id: number;

  items: CartItem[];
  taxes: any[];
  total_tax: number;

  customer: Customer;
  created_by: string;
  address: string;
  store: string;
  current_status: string;
  time_line: string;
}
