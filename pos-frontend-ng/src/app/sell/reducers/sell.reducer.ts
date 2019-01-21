import {
    sellUiActions, CLOSE_CART, OPEN_CART, CLOSE_DIALOGUES, OPEN_DIALOGUES, START_PAYMENT,
    COMPLETE_PAYMENT, START_SELL_SUBMITION, COMPLETE_SELL_SUBMITION,
} from '../actions/sell-ui.action';

export interface State {
    isCartOpen: boolean;
    isProcessingPayment: boolean;
    isDialogOpen: boolean;
    isSubmittingSale: boolean;
}

const initialState: State = {
    isCartOpen: false,
    isProcessingPayment: false,
    isDialogOpen: false,
    isSubmittingSale: false,
};

export function sellUiReducer(state = initialState, action: sellUiActions) {
    switch (action.type) {
        case OPEN_CART:
            return {
                isCartOpen: true, isProcessingPayment: state.isProcessingPayment,
                isDialogOpen: state.isDialogOpen, isSubmittingSale: state.isSubmittingSale
            };
        case CLOSE_CART:
            return {
                isCartOpen: false, isProcessingPayment: state.isProcessingPayment,
                isDialogOpen: state.isDialogOpen, isSubmittingSale: state.isSubmittingSale
            };
        case OPEN_DIALOGUES:
            return {
                isCartOpen: state.isCartOpen, isProcessingPayment: state.isProcessingPayment,
                isDialogOpen: true, isSubmittingSale: state.isSubmittingSale
            };
        case CLOSE_DIALOGUES:
            return {
                isCartOpen: state.isCartOpen, isProcessingPayment: state.isProcessingPayment,
                isDialogOpen: false, isSubmittingSale: state.isSubmittingSale
            };
        case START_PAYMENT:
            return {
                isCartOpen: state.isCartOpen, isProcessingPayment: true,
                isDialogOpen: state.isDialogOpen, isSubmittingSale: state.isSubmittingSale
            };
        case COMPLETE_PAYMENT:
            return {
                isCartOpen: state.isCartOpen, isProcessingPayment: false,
                isDialogOpen: state.isDialogOpen, isSubmittingSale: state.isSubmittingSale
            };
        case START_SELL_SUBMITION:
            return {
                isCartOpen: state.isCartOpen, isProcessingPayment: state.isProcessingPayment,
                isDialogOpen: state.isDialogOpen, isSubmittingSale: true
            };
        case COMPLETE_SELL_SUBMITION:
            return {
                isCartOpen: state.isCartOpen, isProcessingPayment: state.isProcessingPayment,
                isDialogOpen: state.isDialogOpen, isSubmittingSale: false
            };
        default:
            return state;
    }
}

export const getIsCartOpen = (state: State) => state.isCartOpen;
export const getIsDialogOpen = (state: State) => state.isDialogOpen;
export const getIsProcessingPayment = (state: State) => state.isProcessingPayment;
export const getIsSubmittingSale = (state: State) => state.isSubmittingSale;
