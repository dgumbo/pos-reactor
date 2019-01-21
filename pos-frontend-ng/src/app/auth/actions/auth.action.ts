import {Action} from '@ngrx/store';
import { JwtToken} from '../models';

export const SET_AUTHENTICATED = '[Auth] Set Authenticated';
export const SET_UNAUTHENTICATED = '[Auth] Set UnAuthenticated';
 

export class SetAuthenticated implements Action {
    readonly type = SET_AUTHENTICATED; 
    constructor(public readonly jwtToken: JwtToken ) { /*console.log("jwtToken:", this.jwtToken );*/ }  
} 

export class SetUnAuthenticated implements Action {
    readonly type = SET_UNAUTHENTICATED;
}

export type AuthActions = SetAuthenticated | SetUnAuthenticated;