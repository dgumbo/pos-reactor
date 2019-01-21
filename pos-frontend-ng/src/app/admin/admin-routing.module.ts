import { Routes, RouterModule } from '@angular/router';
import { AdminComponent } from './admin/admin.component';
import { UsersComponent } from './users/users.component';
import { NgModule } from '@angular/core';

const adminRoutes: Routes = [
    { path: '', component: AdminComponent },
    { path: 'users', component: UsersComponent },
];

@NgModule({
    imports: [RouterModule.forChild(adminRoutes)],
    exports: [RouterModule]
})
export class AdminRoutingModule { }
