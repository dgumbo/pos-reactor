import {Action} from '@ngrx/store'; 
import { Menu } from 'shared/models/menu';

export const SET_USER_MENUS = '[NAV_MENU] Set User Menus';
export const UNSET = '[NAV_MENU] UnSet Menus';

export class SetUserMenus implements Action {
    readonly type = SET_USER_MENUS;
    constructor(public readonly menus: Menu[]) {}
}

export class UnSetMenus implements Action {
    readonly type = UNSET;
}

export type navMenuActions = SetUserMenus | UnSetMenus;