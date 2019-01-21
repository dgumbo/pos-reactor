import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { SellRoutingModule } from './sell-routing.module';
import { SharedModule } from 'shared/shared.module';
import { FormsModule } from '@angular/forms';
import { PaymentLineModalFormComponent } from './components/payment/payment-line-modal-form/payment-line-modal-form.component';
import { PaymentModalComponent } from './components/payment/payment-modal/payment-modal.component';
import { StockItemNotFoundModalComponent } from './components/stock-item-not-found-modal/stock-item-not-found-modal.component';
import { CartEmptyModalComponent } from './components/cart-empty-modal/cart-empty-modal.component';
import { SellService, SellPrintService, PosService } from './services';
import { SellHomeComponent, SellCartComponent, ItemListComponent } from './components/sell';
import { PrintLayoutComponent, InvoiceComponent } from './components/print';


@NgModule({
    declarations: [
        ItemListComponent,
        SellCartComponent,
        SellHomeComponent,
        PaymentModalComponent,
        PrintLayoutComponent,
        InvoiceComponent,
        PaymentLineModalFormComponent,
        StockItemNotFoundModalComponent,
        CartEmptyModalComponent,
    ],
    imports: [
        CommonModule,
        FormsModule,
        SharedModule,
        SellRoutingModule,
    ],
    entryComponents: [
        PaymentModalComponent,
        PaymentLineModalFormComponent,
        StockItemNotFoundModalComponent,
        CartEmptyModalComponent,
    ],
    providers: [
        SellService,
        SellPrintService,
        PosService,
    ]
})
export class SellModule { }
