import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatTableModule } from '@angular/material/table';

import { ReportsRoutingModule } from './reports-routing.module';
import { ReportsHomeComponent } from './components/reports-home/reports-home.component';
import { ReportViewComponent } from './components/report-view/report-view.component';
import { SharedModule } from 'shared/shared.module';
import { DynamicFormModule } from '../dynamic-form/dynamic-form.module';
import { FormsModule } from '@angular/forms';
import { StaticReportViewComponent } from './components/static-report-view/static-report-view.component';
import { StaticReportsDataService } from 'shared/services/static-reports-data.service';
import { ReportsPrintService } from './services/reports-print.service';
import { ReOrderPrintComponent } from './components/receipt-print-layout/re-order-print/re-order-print.component';
import { ReportsReceiptPrintLayoutComponent } from './components/receipt-print-layout/reports-receipt-print-layout.component';
import { CurrentStockReceiptReportComponent } from './components/receipt-print-layout/curent-stock/curent-stock.component';
import {
  SalesSummaryReceiptPrintComponent
} from './components/receipt-print-layout/sales-summary-receipt-print/sales-summary-receipt-print.component';
import { LineChartComponent } from './components/line-chart/line-chart.component';
import { ReportsComponent } from './components/other-reports/reports.component';
// import {AuthModule} from '../auth/auth.module';

@NgModule({
  imports: [
    CommonModule,
    SharedModule,
    ReportsRoutingModule,
    DynamicFormModule,
    FormsModule,
    // AuthModule,
  ],
  declarations: [
    ReportsHomeComponent,
    ReportViewComponent,
    StaticReportViewComponent,

    ReportsReceiptPrintLayoutComponent,
    ReOrderPrintComponent,

    SalesSummaryReceiptPrintComponent,
    CurrentStockReceiptReportComponent,

    LineChartComponent,
    ReportsComponent,
  ],
  providers: [
    StaticReportsDataService,
    ReportsPrintService,
  ]
})
export class ReportsModule { }
