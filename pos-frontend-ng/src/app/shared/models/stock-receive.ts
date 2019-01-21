import { StockReceiveItem } from './stock-receive-item';
import { WorkFlowType } from './enums/work-flow-type';


export interface StockReceive {
  id?: number;
  stockReceiveItems: StockReceiveItem[];
  remarks: String;
  totalCost: number;
  workFlowType: WorkFlowType;
  receiptDate: Date;
  receiptDateTime: Date;
  recieveStatus: String;
}
