import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { UsersComponent } from './users/users.component';
import { AdminComponent } from './admin/admin.component';

import { ParseUserRolePipe } from './users/parse-user-role.pipe';
import { PricesComponent } from './prices/prices.component';
import { SharedModule } from 'shared/shared.module'; 
import { SupplierDataService } from '../masters/services/supplier-data.service';
import { AdminRoutingModule } from './admin-routing.module';
import { AddProductModalFormComponent } from './add-product-modal-form/add-product-modal-form.component';
import { ScheduleOfPriceDataService } from './services/schedule-of-price-data.service';
import { NegativeStockSalesComponent } from './stock/negative-stock-sales/negative-stock-sales.component';

@NgModule({
  imports: [
    AdminRoutingModule,
    CommonModule,
    FormsModule,
    SharedModule
  ],
  declarations: [
    UsersComponent,
    AdminComponent,
    ParseUserRolePipe,
    PricesComponent,
    AddProductModalFormComponent,
    NegativeStockSalesComponent,
  ],
  providers: [
    SupplierDataService,
    ScheduleOfPriceDataService,
    // {provide: MAT_DIALOG_DEFAULT_OPTIONS, useValue: {hasBackdrop: false}},
  ],
  entryComponents: [
  ]
})
export class AdminModule { }
