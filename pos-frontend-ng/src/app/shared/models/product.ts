import { Brand } from './brand';
import { Stock } from './stock';
import { ProductCategory, ScheduleOfPrice } from 'app/shared/models';
import { CurrentStock } from './current-stock';
import { BasicInterface } from './basic-interface';

export interface Product extends BasicInterface {
  name: string;
  description?: string;
  brand?: Brand;
  sub_description?: string;
  is_loose?: boolean;
  barcode?: string;
  shortcode?: string;
  tags?: string;
  stocks?: Stock[];

  bondPrice?: number;
  item_type?: string;
  img_name?: string;
  img_url?: string;
  productCategory: ProductCategory;
  scheduleOfPrices?: ScheduleOfPrice[];

  lastReceiptCostRate: number;
  weightedAvarageCost: number;

  currentStock?: CurrentStock;

}
