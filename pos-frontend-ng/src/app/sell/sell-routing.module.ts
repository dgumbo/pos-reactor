import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { SellHomeComponent } from './components/sell';
import { PrintLayoutComponent, InvoiceComponent } from './components/print';

const routes: Routes = [
  { path: '', component: SellHomeComponent },
  { path: ':date-sell', component: SellHomeComponent },
  {
    path: 'print',
    outlet: 'print',
    component: PrintLayoutComponent,
    children: [
      { path: 'invoice/:sellId', component: InvoiceComponent }
    ]
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class SellRoutingModule { }
