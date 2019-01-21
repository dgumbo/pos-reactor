import { BasicInterface } from '../basic-interface';
import { StockTakeLine } from './stock-take-line';

export interface StockTake extends BasicInterface {
  stockTakeLines: StockTakeLine[];
  remarks: String;
  stockTakeDate: Date;
  stockTakeStatus: String;
}
