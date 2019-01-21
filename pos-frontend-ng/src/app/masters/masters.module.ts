import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { MastersRoutingModule } from './masters-routing.module';
import { SharedModule } from 'shared/shared.module';
import { SupplierModalFormComponent } from './components/suppliers/supplier-modal-form/supplier-modal-form.component';
import { SupplierListComponent } from './components/suppliers/supplier-list/supplier-list.component';
import { SupplierDataService } from './services/supplier-data.service';
import { MastersHomeComponent } from './components/masters-home/masters-home.component';
import { FormsModule } from '@angular/forms';
import { ProductsComponent } from './components/stock-items/products.component';
import { PayementTypeListComponent } from './components/payment-type/payement-type-list/payement-type-list.component';
import { PayementTypeModalFormComponent } from './components/payment-type/payement-type-modal-form/payement-type-modal-form.component';
import { PaymentTypeService } from './services/payment-type.service';
import { CurrencyListComponent } from './components/currency/currency-list/currency-list.component';
import { CurrencyModalFormComponent } from './components/currency/currency-modal-form/currency-modal-form.component';
import { CurrencyDataService } from './services/currency-data.service';
import {
  ProductCategoryModalFormComponent
} from './components/product-category/product-category-modal-form/product-category-modal-form.component';
import { ProductCategoryListComponent } from './components/product-category/product-category-list/product-category-list.component';
import { ProductCategoryDataService } from 'shared/services';
import { AddProductModalComponent } from './components/stock-items/add-product-modal/add-product-modal.component';
import { ExchangeRateModalFormComponent } from './components/exchange-rates/exchange-rate-modal-form/exchange-rate-modal-form.component';
import { ExchangeRatesListComponent } from './components/exchange-rates/exchange-rates-list/exchange-rates-list.component';
import { PriceListComponent } from './components/update-price/price-list/price-list.component';
import { PriceUpdateModalFormComponent } from './components/update-price/price-update-modal-form/price-update-modal-form.component';

@NgModule({
  declarations: [
    CurrencyListComponent,
    CurrencyModalFormComponent,
    SupplierModalFormComponent,
    SupplierListComponent,
    MastersHomeComponent,
    ProductsComponent,
    PayementTypeListComponent,
    PayementTypeModalFormComponent,
    ProductCategoryListComponent,
    ProductCategoryModalFormComponent,
    AddProductModalComponent,
    ExchangeRateModalFormComponent,
    ExchangeRatesListComponent,
    PriceListComponent,
    PriceUpdateModalFormComponent,
  ],
  imports: [
    CommonModule,
    FormsModule,
    SharedModule,
    MastersRoutingModule
  ],
  entryComponents: [
    CurrencyModalFormComponent,
    SupplierModalFormComponent,
    PayementTypeModalFormComponent,
    ProductCategoryModalFormComponent,
    AddProductModalComponent,
    PriceUpdateModalFormComponent,
  ],
  providers: [
    CurrencyDataService,
    SupplierDataService,
    PaymentTypeService,
    ProductCategoryDataService,
  ]
})
export class MastersModule { }
