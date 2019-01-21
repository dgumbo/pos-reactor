import {NgModule} from '@angular/core';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';

import {AuthRoutingModule, authModuleRoutedComponents} from './auth-routing.module';
import {AuthService, TokenStorageService} from './services';
import {HTTP_INTERCEPTORS} from '@angular/common/http';
import {AuthInterceptor, JwtErrorInterceptor} from './interceptors';
import {StoreRouterConnectingModule} from '@ngrx/router-store';
import {SharedModule} from 'shared/shared.module'; 
import {CommonModule} from '@angular/common';

@NgModule({
    imports: [
        FormsModule,
        ReactiveFormsModule,
        SharedModule,
        CommonModule,
        AuthRoutingModule,
    ],
    declarations: [
        ...authModuleRoutedComponents
    ],
    providers: [
        AuthService,
        TokenStorageService,
        StoreRouterConnectingModule,
        {
            provide: HTTP_INTERCEPTORS,
            useClass: AuthInterceptor,
            multi: true
        },
        {
            provide: HTTP_INTERCEPTORS,
            useClass: JwtErrorInterceptor,
            multi: true
        }
    ],
})
export class AuthModule {}
