import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { SupplierListComponent } from './components/suppliers/supplier-list/supplier-list.component';
import { MastersHomeComponent } from './components/masters-home/masters-home.component';
import { ProductsComponent } from './components/stock-items/products.component';
import { PayementTypeListComponent } from './components/payment-type/payement-type-list/payement-type-list.component';
import { CurrencyListComponent } from './components/currency/currency-list/currency-list.component';
import { ProductCategoryListComponent } from './components/product-category/product-category-list/product-category-list.component';
import { PriceListComponent } from './components/update-price/price-list/price-list.component';
 
const routes: Routes = [
  { path: '', component: MastersHomeComponent },
  { path: 'stock-items', component: ProductsComponent },
  { path: 'suppliers', component: SupplierListComponent },
  { path: 'currency', component: CurrencyListComponent },
  { path: 'payment-type', component: PayementTypeListComponent },
  { path: 'product-category', component: ProductCategoryListComponent },
  { path: 'update-price-by-ratio', component: PriceListComponent },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class MastersRoutingModule { }
