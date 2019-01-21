import { NgModule } from '@angular/core';
import { CommonModule, LocationStrategy, HashLocationStrategy } from '@angular/common';
import { RouterModule, Routes, CanActivate } from '@angular/router';

import { WelcomeComponent } from 'welcome/welcome.component';
import { LoginComponent } from 'welcome/login/login.component';
import { ResetpasswordComponent } from 'welcome/resetpassword/resetpassword.component';
import { PageNotFoundComponent } from './page-not-found/page-not-found.component';

const appRoutes: Routes = [
    // { path: '', redirectTo: '/welcome', pathMatch: 'full' },
    { path: '', redirectTo: '/sell', pathMatch: 'full' },
    { path: 'welcome', component: WelcomeComponent/*, canActivate: [AuthGuard]*/ },
    { path: 'login', component: LoginComponent },
    { path: 'resetpassword', component: ResetpasswordComponent },
    { path: 'admin', loadChildren: './admin/admin.module#AdminModule' },
    { path: 'reports', loadChildren: './reports/reports.module#ReportsModule' },
    { path: 'stocks', loadChildren: './stocks/stocks.module#StocksModule' },
    { path: 'sell', loadChildren: './sell/sell.module#SellModule' },
    { path: 'masters', loadChildren: './masters/masters.module#MastersModule' },
    { path: 'dayend', loadChildren: './dayend/dayend.module#DayendModule' },
    { path: '**', component: PageNotFoundComponent }
];

@NgModule({
    imports: [
        CommonModule,
        RouterModule.forRoot(appRoutes),
    ],
    providers: [
        //  AuthGuard,
        //  AdminGuard,
        { provide: LocationStrategy, useClass: HashLocationStrategy },
    ],
    exports: [RouterModule],
    declarations: [],
})
export class AppRoutingModule { }
