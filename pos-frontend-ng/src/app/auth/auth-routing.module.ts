import {NgModule} from '@angular/core';
import {Routes, RouterModule} from '@angular/router';   
import {HomeComponent, LoginComponent, LogoutComponent, RoleUnitSelectComponent } from './components';
 
const routes: Routes = [ 
    {
        path: '',
        component: HomeComponent
    },
    {
        path: 'login',
        component: LoginComponent
    },
    {
        path: 'logout',
        component: LogoutComponent
    },
    {
        path: 'roleunit',
        component: RoleUnitSelectComponent
    }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class AuthRoutingModule {}

export const authModuleRoutedComponents = [
    LoginComponent,
    LogoutComponent,
    HomeComponent,  
    RoleUnitSelectComponent,
]