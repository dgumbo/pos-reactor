import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { ReportViewComponent, ReportsHomeComponent } from './components';
import { StaticReportViewComponent } from './components/static-report-view/static-report-view.component';
import { ReOrderPrintComponent } from './components/receipt-print-layout/re-order-print/re-order-print.component';
import { ReportsReceiptPrintLayoutComponent } from './components/receipt-print-layout/reports-receipt-print-layout.component';
import { CurrentStockReceiptReportComponent } from './components/receipt-print-layout/curent-stock/curent-stock.component';
import {
    SalesSummaryReceiptPrintComponent
} from './components/receipt-print-layout/sales-summary-receipt-print/sales-summary-receipt-print.component';

const routes: Routes = [
    {
        path: '',
        component: ReportsHomeComponent
    },
    {
        path: 'report-view-by-id/:reportId',
        component: ReportViewComponent
    },
    {
        path: 'report-view-by-name/:reportName',
        component: ReportViewComponent
    },
    {
        path: 'static-report-view-by-name/:reportName/:menuFunction',
        component: StaticReportViewComponent
    },
    {
        path: 'report-receipt-print',
        component: ReportsReceiptPrintLayoutComponent,
        outlet: 'print',
        children: [
            { path: 're-order', component: ReOrderPrintComponent },
            { path: 'sales-summary-summarized', component: SalesSummaryReceiptPrintComponent },
            { path: 'sales-summary-detailed', component: SalesSummaryReceiptPrintComponent },
            { path: 'current-stock', component: CurrentStockReceiptReportComponent },
            { path: 'stock-out', component: ReOrderPrintComponent },
        ]
    },
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class ReportsRoutingModule { }
