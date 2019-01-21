import { Component, OnInit, Input } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { StockReOrderView } from 'shared/models/reports/stock-re-order-view';
import { StaticReportsDataService } from 'shared/services/static-reports-data.service';
import { ReportInformation } from 'shared/models/reports/report-information';
import { MatSnackBar } from '@angular/material';
import { HttpParams } from '@angular/common/http';
import { ReportsPrintService } from 'app/reports/services/reports-print.service';

@Component({
  selector: 'app-re-order-print',
  templateUrl: './re-order-print.component.html',
  styleUrls: ['./re-order-print.component.scss']
})
export class ReOrderPrintComponent implements OnInit {

  date = new Date();
  totalOrderCost = 0;

  stockReOrders: StockReOrderView[] = [];
  returnUrl: string = null;

  constructor(private route: ActivatedRoute,
    private reportsPrintService: ReportsPrintService,
    private staticReportsDataService: StaticReportsDataService,
    private snackbar: MatSnackBar, ) {
  }

  ngOnInit() {
    this.returnUrl = this.route.snapshot.params['returnUrl'];
    // console.log(this);
    console.log('this.returnUrl', this.returnUrl);

    this.getReportData();
  }

  getReportData() {

    const params = new HttpParams()
      .append('stockItemName', '');

    this.staticReportsDataService.getReportPreview('/stocks/re-order', params)
      .subscribe(
        (res: ReportInformation) => {
          // console.log('res.reportData');
          // console.log(res.reportData);

          res.reportData.forEach((rd: any) => {
            this.totalOrderCost += rd.orderCost;
          });

          this.stockReOrders = res.reportData;
          this.reportsPrintService.onDataReady();
        }
        , (error: Error) => this.handleError(error)
      );
  }

  handleError(error: Error) {

    this.snackbar.open(error.message, null, {
      duration: 5000
    });
  }

}
