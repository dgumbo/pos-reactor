import { BasicInterface } from 'shared/models/basic-interface';

export interface PaymentType extends BasicInterface {
    name: string;
    defaultPaymentType: boolean;
}
