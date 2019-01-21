import { BrowserModule } from '@angular/platform-browser';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { NgModule } from '@angular/core';

import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import { AppComponent } from './app.component';
import { NavComponent } from './nav/nav.component';

import { WelcomeComponent } from './welcome/welcome.component';
import { PageNotFoundComponent } from './page-not-found/page-not-found.component';
import { LoginComponent } from './welcome/login/login.component';
import { ResetpasswordComponent } from './welcome/resetpassword/resetpassword.component';
import { AppRoutingModule } from './app-routing.module';
import { SharedModule } from 'shared/shared.module';
import { MAT_DIALOG_DEFAULT_OPTIONS } from '@angular/material';
import { NotifyService } from 'auth/services/notify.service';
import { StoreModule } from '@ngrx/store';
import { reducers } from './app.reducer';

@NgModule({
  declarations: [
    AppComponent,
    NavComponent,
    WelcomeComponent,
    PageNotFoundComponent,
    LoginComponent,
    ResetpasswordComponent,
  ],
  imports: [
    AppRoutingModule,
    BrowserModule,
    SharedModule,
    BrowserAnimationsModule,
    FormsModule,
    ReactiveFormsModule,
    StoreModule.forRoot(reducers),
  ],
  providers: [
    { provide: MAT_DIALOG_DEFAULT_OPTIONS, useValue: { hasBackdrop: false } },
    NotifyService,
    //  AuthService,
  ],
  bootstrap: [AppComponent],
})
export class AppModule { }
