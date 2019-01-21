import { Action } from '@ngrx/store';

export const CLOSE_CART = '[UI] Set Cart Is Clossed';
export const OPEN_CART = '[UI] Re-Open Shopping Cart';
export const CLOSE_DIALOGUES = '[UI] Set Dialogues Open';
export const OPEN_DIALOGUES = '[UI] Set Dialogues Closed';
export const START_PAYMENT = '[UI] Set Procesing Payment';
export const COMPLETE_PAYMENT = '[UI] Finish Procesing Payment';
export const START_SELL_SUBMITION = '[UI] Set Is Submitting Sale';
export const COMPLETE_SELL_SUBMITION = '[UI] Set Sale Submittion Complete';

export class StartSellSubmition implements Action {
    readonly type = START_SELL_SUBMITION;
}

export class SellSubmitionComplete implements Action {
    readonly type = COMPLETE_SELL_SUBMITION;
}

export class OpenCart implements Action {
    readonly type = OPEN_CART;
}

export class CloseCart implements Action {
    readonly type = CLOSE_CART;
}

export class ProccessPayment implements Action {
    readonly type = START_PAYMENT;
}

export class PaymentCompleted implements Action {
    readonly type = COMPLETE_PAYMENT;
}

export class OpenModalDialogue implements Action {
    readonly type = OPEN_DIALOGUES;
}

export class CloseModalDialogue implements Action {
    readonly type = CLOSE_DIALOGUES;
}

export type sellUiActions = StartSellSubmition | SellSubmitionComplete | OpenCart |
CloseCart | ProccessPayment | PaymentCompleted | OpenModalDialogue | CloseModalDialogue;
