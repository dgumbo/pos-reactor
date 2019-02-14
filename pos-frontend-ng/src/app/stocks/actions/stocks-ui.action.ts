import { Action } from '@ngrx/store';

export const CLOSE_DIALOGUES = '[UI] Set Dialogues Open';
export const OPEN_DIALOGUES = '[UI] Set Dialogues Closed';
export const START_STOCK_TAKE_SUBMITION = '[UI] Set Is Submitting Sale';
export const COMPLETE_STOCK_TAKE_SUBMITION = '[UI] Set Sale Submittion Complete';
export const START_STOCK_RECEIVE_SUBMITION = '[UI] Set Is Submitting Sale';
export const COMPLETE_STOCK_RECEIVE_SUBMITION = '[UI] Set Sale Submittion Complete';

export class StartStockReceiveSubmition implements Action {
    readonly type = START_STOCK_RECEIVE_SUBMITION;
}

export class CompleteStockReceiveSubmition implements Action {
    readonly type = COMPLETE_STOCK_RECEIVE_SUBMITION;
}

export class StartStockTakeSubmition implements Action {
    readonly type = START_STOCK_TAKE_SUBMITION;
}

export class CompleteStockTakeSubmition implements Action {
    readonly type = COMPLETE_STOCK_TAKE_SUBMITION;
}

export class OpenModalDialogue implements Action {
    readonly type = OPEN_DIALOGUES;
}

export class CloseModalDialogue implements Action {
    readonly type = CLOSE_DIALOGUES;
}

export type stocksUiActions = StartStockTakeSubmition | CompleteStockTakeSubmition
    | OpenModalDialogue | CloseModalDialogue;
