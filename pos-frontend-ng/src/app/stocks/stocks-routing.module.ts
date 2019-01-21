import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { CurrentStockComponent } from './components/current-stock/current-stock.component';
import { PriceListComponent } from './components/price-update/price-list/price-list.component';
import { StockReceiveComponent } from './components/stock-receive/receive/stock-receive.component';
import { ReceiveListComponent } from './components/stock-receive/receive-list/receive-list.component';
import { StocksHomeComponent } from './components/stocks-home/stocks-home.component';
import { StockTakeListComponent } from './components/stock-take/stock-take-list/stock-take-list.component';
import { StockTakeFormComponent } from './components/stock-take/stock-take-form/stock-take-form.component';

const routes: Routes = [
  { path: '', component: StocksHomeComponent },
  { path: 'items', redirectTo: '/masters/stock-items' },
  { path: 'stock-receive', component: ReceiveListComponent },
  { path: 'stock-receive/receive', component: StockReceiveComponent },
  { path: 'stock-receive/preview/:receiveId', component: StockReceiveComponent },
  { path: 'prices', component: PriceListComponent },
  { path: 'current', component: CurrentStockComponent },
  { path: 'stock-take', component: StockTakeListComponent },
  { path: 'stock-take/take', component: StockTakeFormComponent },
  { path: 'stock-take/preview/:stockTakePreviewId', component: StockTakeFormComponent },
  { path: 'stock-take/finalize/:stockTakeFinalizeId', component: StockTakeFormComponent },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class StocksRoutingModule { }
