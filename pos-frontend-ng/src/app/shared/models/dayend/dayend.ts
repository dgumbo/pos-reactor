import { DayendItem } from './dayend-item';
import { BasicInterface } from '../basic-interface';
import { PaymentType } from 'app/masters/models/payment-type';

export interface Dayend extends BasicInterface {
    dayendItems: DayendItem[];
    dayStartDateTime: Date;
    dayEndDate: Date;
    dayEndDateTime: Date;
}
