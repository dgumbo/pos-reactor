import * as fromShared from './shared/reducers';
import * as fromAuth from './auth/reducers/auth.reducer';
import * as fromSell from './sell/reducers/sell.reducer';

import { createFeatureSelector, ActionReducerMap, createSelector } from '@ngrx/store';

export interface State {
    ui: fromShared.State;
    auth: fromAuth.State;
    menus: fromShared.Menus;
    sell: fromSell.State;
}

export const reducers: ActionReducerMap<State> = {
    ui: fromShared.uiReducer,
    auth: fromAuth.authReducer,
    menus: fromShared.navMenuReducer,
    sell: fromSell.sellUiReducer,
};

export const getUiState = createFeatureSelector<fromShared.State>('ui');
export const getIsLoading = createSelector(getUiState, fromShared.getIsLoading);
export const getIsDownloading = createSelector(getUiState, fromShared.getIsDownloading);

export const getUserMenusState = createFeatureSelector<fromShared.Menus>('menus');
export const getUserMenus = createSelector(getUserMenusState, fromShared.getUserMenus);

export const getAuthState = createFeatureSelector<fromAuth.State>('auth');
export const getIsAuthenticated = createSelector(getAuthState, fromAuth.getIsAuthenticated);
export const getIsAdmin = createSelector(getAuthState, fromAuth.getIsAdmin);
export const getRoleUserUnit = createSelector(getAuthState, fromAuth.getRoleUserUnit);
export const getJwtToken = createSelector(getAuthState, fromAuth.getJwtToken);

export const getSellState = createFeatureSelector<fromSell.State>('sell');
export const getIsCartOpen = createSelector(getSellState, fromSell.getIsCartOpen);
export const getIsDialogOpen = createSelector(getSellState, fromSell.getIsDialogOpen);
export const getIsProcessingPayment = createSelector(getSellState, fromSell.getIsProcessingPayment);
export const getIsSubmittingSale = createSelector(getSellState, fromSell.getIsSubmittingSale);
