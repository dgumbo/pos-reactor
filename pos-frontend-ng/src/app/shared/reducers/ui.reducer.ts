import { uiActions, START_LOADING, STOP_LOADING, STOP_DOWNLOADING, START_DOWNLOADING } from '../actions';

export interface State {
    isLoading: boolean;
    isDownloading: boolean;
}

const initialState: State = {
    isLoading: false,
    isDownloading: false,
};

export function uiReducer(state = initialState, action: uiActions) {
    switch (action.type) {
        case START_LOADING:
            return { isLoading: true, isDownloading: state.isDownloading };
        case STOP_LOADING:
            return { isLoading: false, isDownloading: state.isDownloading };
        case START_DOWNLOADING:
            return { isDownloading: true, isLoading: state.isLoading };
        case STOP_DOWNLOADING:
            return { isDownloading: false, isLoading: state.isLoading };
        default:
            return state;
    }
}

export const getIsLoading = (state: State) => state.isLoading;
export const getIsDownloading = (state: State) => state.isDownloading;
