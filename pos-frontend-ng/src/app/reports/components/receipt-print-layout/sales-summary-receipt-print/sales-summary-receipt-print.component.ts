import { Component, OnInit, Input } from '@angular/core';
import { ActivatedRoute, UrlSegment } from '@angular/router';
import { ReportsPrintService } from 'app/reports/services/reports-print.service';
import { ReportInformation } from 'shared/models/reports/report-information';
import { HttpParams } from '@angular/common/http';
import { MatSnackBar } from '@angular/material';
import { StaticReportsDataService } from 'shared/services/static-reports-data.service';
import { StockReOrderView } from 'shared/models/reports/stock-re-order-view';

import * as moment from 'moment/moment';
import { SalesSummaryView } from 'shared/models/reports/sales-summary-view';

@Component({
  selector: 'app-sales-summary-receipt-print',
  templateUrl: './sales-summary-receipt-print.component.html',
  styleUrls: ['./sales-summary-receipt-print.component.scss']
})
export class SalesSummaryReceiptPrintComponent implements OnInit {

  date = new Date();
  fromDate = new Date();
  toDate = new Date();
  totalOrderCost = 0;

  stockReOrders: StockReOrderView[] = [];
  returnUrl: string = null;
  summarised = false;

  constructor(private route: ActivatedRoute,
    private reportsPrintService: ReportsPrintService,
    private staticReportsDataService: StaticReportsDataService,
    private snackbar: MatSnackBar, ) {
    this.fromDate.setHours(0, 0, 0, 0);
    // this.fromDate.setDate(1);
    this.toDate.setHours(23, 59, 59, 999);

    this.returnUrl = this.route.snapshot.params['returnUrl'];
    this.summarised = this.route.snapshot.params['summarised'];

    this.route.url.subscribe(
      (res: UrlSegment[]) => {
        const path = res && res[0] ? res[0].path : '';

        this.summarised = path === 'sales-summary-detailed' ? false : true;
        console.log('summarised : ', this.summarised);
      }
    );
    // console.log('this.returnUrl', this.returnUrl);
  }

  ngOnInit() {
    this.getReportData();
  }

  getReportData() {
    const fromDateStr = moment(this.fromDate).format('YYYY-MM-DD HH:mm:ss');
    const toDateStr = moment(this.toDate).format('YYYY-MM-DD HH:mm:ss');

    const params = new HttpParams()
      .append('stockItemName', '')
      .append('fromDate', fromDateStr)
      .append('toDate', toDateStr);

    this.staticReportsDataService.getReportPreview('/sales/summary', params)
      .subscribe(
        (res: ReportInformation) => {
          res.reportData.forEach((rd: SalesSummaryView) => {
            this.totalOrderCost += rd.totalPrice;
          });

          this.summariseData(res);

          this.stockReOrders = res.reportData;
          this.reportsPrintService.onDataReady();
        }
        , (error: Error) => this.handleError(error)
      );
  }

  summariseData(res: ReportInformation) {
    if (this.summarised) {
      let rd = 0;
      while (rd < res.reportData.length) {
        const prod: SalesSummaryView = res.reportData[rd];

        const count = res.reportData.filter((qr: SalesSummaryView) => qr.stockItem === prod.stockItem).length;
        if (count >= 2) {
          let xd = rd + 1;
          while (xd < res.reportData.length) {
            const st: SalesSummaryView = res.reportData[xd];
            if (st.stockItem === prod.stockItem) {
              prod.quantity += st.quantity;
              prod.totalPrice += st.totalPrice;

              res.reportData.splice(xd, 1);
              xd -= 1;
            }
            xd += 1;
          }
        }
        rd += 1;
      }
    }
  }

  handleError(error: Error) {

    this.snackbar.open(error.message, null, {
      duration: 5000
    });
  }

}
