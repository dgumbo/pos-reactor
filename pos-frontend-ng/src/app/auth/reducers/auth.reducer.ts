import {AuthActions, SET_AUTHENTICATED, SET_UNAUTHENTICATED} from '../actions';
import {RoleUserUnit, JwtToken} from '../models';
import {JwtHelperService} from '@auth0/angular-jwt';

const helper = new JwtHelperService();

export interface State {
    isAuthenticated: boolean,
    isAdmin: boolean,
    roleUserUnit: RoleUserUnit,  
    jwtToken: JwtToken, 
}

const initialState: State = {
    isAuthenticated: false,
    isAdmin: false, 
    roleUserUnit: null , 
    jwtToken: null , 
};

export function authReducer(state = initialState, action: AuthActions) {
    switch (action.type) {
        case SET_AUTHENTICATED: 
            var jwtToken:JwtToken = action.jwtToken ; 
//            console.log ("jwtToken:");
//            console.log (jwtToken);
//            console.log ("token:");
//            console.log (token); 
            
            var decodedToken = helper.decodeToken(jwtToken.token) ;
            var roleUserUnit: RoleUserUnit = decodedToken.roleUserUnit || null  ;
            
            var isAdmin:boolean = roleUserUnit &&  roleUserUnit.role &&  roleUserUnit.role.name.toLowerCase() === 'admin'; 
            return {
                isAuthenticated: true,
                isAdmin: isAdmin ,
                roleUserUnit:  roleUserUnit, 
                jwtToken: jwtToken, 
            }; 

        case SET_UNAUTHENTICATED:
            return {
                isAuthenticated: false,
                isAdmin: false, 
                roleUserUnit: null,
                jwtToken:  null , 
            };
        default: {
            return state;
        }
    }
}

export const getIsAuthenticated = (state: State) => state.isAuthenticated;
export const getIsAdmin = (state: State) => state.isAdmin;
export const getRoleUserUnit = (state: State) => state.roleUserUnit ;
export const getJwtToken = (state: State) => state.jwtToken ;

