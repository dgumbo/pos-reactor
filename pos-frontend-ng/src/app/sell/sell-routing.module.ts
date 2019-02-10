import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { SellHomeComponent } from './components/sell';
import { PrintLayoutComponent, InvoiceComponent } from './components/print';
import { ReceiptPreviewComponent } from './components/receipt-preview/receipt-preview.component';

const routes: Routes = [
  { path: '', component: SellHomeComponent },
  {
    path: 'print',
    outlet: 'print',
    component: PrintLayoutComponent,
    children: [
      { path: 'invoice/:sellId', component: InvoiceComponent }
    ]
  },
  {
    path: 'testprint',
    outlet: 'print',
    component: ReceiptPreviewComponent,
    // children: [
    //   { path: 'invoice/:sellId', component: InvoiceComponent }
    // ]
  },
  { path: ':date-sell', component: SellHomeComponent },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class SellRoutingModule { }
