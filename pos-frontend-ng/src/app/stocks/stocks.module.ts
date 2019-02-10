import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { StocksRoutingModule } from './stocks-routing.module';
import { SharedModule } from 'shared/shared.module';
import { StockReceiveModalFormComponent } from './components/stock-receive/modal-form/stock-receive-modal-form.component';
import { CurrentStockComponent } from './components/current-stock/current-stock.component';
import { PriceListComponent } from './components/price-update/price-list/price-list.component';
import { ReceiveListComponent } from './components/stock-receive/receive-list/receive-list.component';
import { StockReceiveComponent } from './components/stock-receive/receive-form/stock-receive.component';
import { StockComponent } from './components/stock/stock.component';
import { FormsModule } from '@angular/forms';
import { SupplierDataService } from 'app/masters/services/supplier-data.service';
import { SearchProductModalComponent } from 'admin/search-product-modal/search-product-modal.component';
import { ScheduleOfPriceDataService } from 'admin/services/schedule-of-price-data.service';
import { BookStockListComponent } from './components/book-count/book-stock-list/book-stock-list.component';
import { BookStockFormComponent } from './components/book-count/book-stock-form/book-stock-form.component';
import { StocksHomeComponent } from './components/stocks-home/stocks-home.component';
import { StockTakeFormComponent } from './components/stock-take/stock-take-form/stock-take-form.component';
import { StockTakeListComponent } from './components/stock-take/stock-take-list/stock-take-list.component';
import { StockItemSearchModalFormComponent } from './components/stock-item-search-modal-form/stock-item-search-modal-form.component';
import { StockTakeService } from './services/stock-take.service';
import { StockTakeItemModalFormComponent } from './components/stock-take/stock-take-item-modal-form/stock-take-item-modal-form.component';
import { StockReceiveDataService } from './services/stock-receive-data.service';
import { CurrentStockDataService } from './services/current-stock-data.service';
import { SupplierModalFormComponent } from 'app/masters/components/suppliers/supplier-modal-form/supplier-modal-form.component';
import { MastersModule } from 'app/masters/masters.module';

@NgModule({
  declarations: [
    StockReceiveModalFormComponent,
    StockComponent,
    StockReceiveComponent,
    ReceiveListComponent,
    PriceListComponent,
    CurrentStockComponent,
    SearchProductModalComponent,
    BookStockListComponent,
    BookStockFormComponent,
    StocksHomeComponent,
    StockTakeFormComponent,
    StockTakeListComponent,
    StockItemSearchModalFormComponent,
    StockTakeItemModalFormComponent,
  ],
  imports: [
    CommonModule,
    FormsModule,
    StocksRoutingModule,
    SharedModule,
    MastersModule
  ],
  providers: [
    SupplierDataService,
    ScheduleOfPriceDataService,
    StockTakeService,
    CurrentStockDataService,
    StockReceiveDataService,
  ],
  entryComponents: [
    StockReceiveModalFormComponent,
    SearchProductModalComponent,
    StockItemSearchModalFormComponent,
    StockTakeItemModalFormComponent,
  ]
})
export class StocksModule { }
