import { navMenuActions, SET_USER_MENUS, UNSET } from '../actions';
import { Menu } from 'shared/models/menu';

export interface Menus {
    menus: Menu[];
}

const initialState: Menus = {
    menus: []
}

export function navMenuReducer(state = initialState, action: navMenuActions) {
    switch (action.type) {
        case SET_USER_MENUS:
            return { menus: action.menus };
        case UNSET:
            return { menus: [] };
        default:
            return state;
    }
}

export const getUserMenus = (state: Menus) => state.menus;