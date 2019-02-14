import {
    stocksUiActions, CLOSE_DIALOGUES, OPEN_DIALOGUES, START_STOCK_TAKE_SUBMITION,
    COMPLETE_STOCK_TAKE_SUBMITION, START_STOCK_RECEIVE_SUBMITION, COMPLETE_STOCK_RECEIVE_SUBMITION,
} from '../actions/stocks-ui.action';



export interface State {
    isDialogOpen: boolean;
    isSubmittingStockTake: boolean;
    isSubmittingStockReceive: boolean;
}

const initialState: State = {
    isDialogOpen: false,
    isSubmittingStockTake: false,
    isSubmittingStockReceive: false,
};

export function stocksUiReducer(state = initialState, action: stocksUiActions) {
    switch (action.type) {
        case START_STOCK_TAKE_SUBMITION:
            return {
                isSubmittingStockTake: true,
                isDialogOpen: state.isDialogOpen,
                isSubmittingStockReceive: state.isSubmittingStockReceive,
            };
        case COMPLETE_STOCK_TAKE_SUBMITION:
            return {
                isSubmittingStockTake: false,
                isDialogOpen: state.isDialogOpen,
                isSubmittingStockReceive: state.isSubmittingStockReceive,
            };
        case OPEN_DIALOGUES:
            return {
                isSubmittingStockTake: state.isSubmittingStockTake,
                isDialogOpen: true,
                isSubmittingStockReceive: state.isSubmittingStockReceive,
            };
        case CLOSE_DIALOGUES:
            return {
                isSubmittingStockTake: state.isSubmittingStockTake,
                isDialogOpen: false,
                isSubmittingStockReceive: state.isSubmittingStockReceive,
            };
        case START_STOCK_RECEIVE_SUBMITION:
            return {
                isSubmittingStockTake: true,
                isDialogOpen: state.isDialogOpen,
                isSubmittingStockReceive: state.isSubmittingStockReceive,
            };
        case COMPLETE_STOCK_RECEIVE_SUBMITION:
            return {
                isSubmittingStockTake: state.isSubmittingStockTake,
                isDialogOpen: state.isDialogOpen,
                isSubmittingStockReceive: false,
            };
        default:
            return state;
    }
}

export const getIsDialogOpen = (state: State) => state.isDialogOpen;
export const getIsSubmittingStockTake = (state: State) => state.isSubmittingStockTake;
export const getIsSubmittingStockReceive = (state: State) => state.isSubmittingStockReceive;

