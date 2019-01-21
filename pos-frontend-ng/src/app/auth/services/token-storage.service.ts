import { Injectable } from '@angular/core';

import { RoleUserUnit } from '../models';
import { Menu } from 'shared/models/menu';

const TOKEN_KEY = 'AuthToken';
const PRE_AUTH_TOKEN_KEY = 'PreAuthToken';
const USER_MENUS_KEY = 'UserMenusToken';
const ROLE_USER_MENU_KEY = 'RoleUserMenuToken';

@Injectable()
export class TokenStorageService {

    constructor() { }

    signOut() {
        this.resetTokens();
        window.sessionStorage.clear();
    }

    public saveToken(token: string) {
        this.resetTokens();
        window.sessionStorage.setItem(TOKEN_KEY, token);
    }

    public getToken(): string {
        return sessionStorage.getItem(TOKEN_KEY);
    }

    public saveRoleUserMenu(roleUserUnit: RoleUserUnit) {
        window.sessionStorage.setItem(ROLE_USER_MENU_KEY, JSON.stringify(roleUserUnit));
    }

    public getRoleUserMenu(): RoleUserUnit {
        const roleUserUnitString = sessionStorage.getItem(ROLE_USER_MENU_KEY);
        if (JSON.parse(roleUserUnitString)) {
            return JSON.parse(roleUserUnitString);
        } else {
            return null;
        }
    }

    saveUserMenus(menus: Menu[]) {
        window.sessionStorage.setItem(USER_MENUS_KEY, JSON.stringify(menus));
    }

    getUserMenus(): Menu[] {
        const userMenusString = sessionStorage.getItem(USER_MENUS_KEY);
        if (JSON.parse(userMenusString)) {
            return JSON.parse(userMenusString);
        } else {
            return null;
        }
    }

    public savePreAuthToken(token: string) {
        this.resetTokens();
        window.sessionStorage.setItem(PRE_AUTH_TOKEN_KEY, token);
    }

    public getPreAuthToken(): string {
        return sessionStorage.getItem(PRE_AUTH_TOKEN_KEY);
    }

    private resetTokens(): void {
        // console.trace();
        window.sessionStorage.removeItem(TOKEN_KEY);
        window.sessionStorage.removeItem(USER_MENUS_KEY);
        window.sessionStorage.removeItem(ROLE_USER_MENU_KEY);
        window.sessionStorage.removeItem(PRE_AUTH_TOKEN_KEY);
    }
}
