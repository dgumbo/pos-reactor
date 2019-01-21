import { Component, OnInit, Input } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ReportsPrintService } from 'app/reports/services/reports-print.service';
import { StaticReportsDataService } from 'shared/services/static-reports-data.service';
import { CurrentStockView } from 'shared/models/reports/current-stock-view';
import { MatSnackBar } from '@angular/material';
import { ReportInformation } from 'shared/models/reports/report-information';
import { HttpParams } from '@angular/common/http';

@Component({
  selector: 'app-curent-stock',
  templateUrl: './curent-stock.component.html',
  styleUrls: ['./curent-stock.component.scss']
})
export class CurrentStockReceiptReportComponent implements OnInit {

  date = new Date();
  totalOrderCost = 0;
  totalSellingValue = 0;

  stockReOrders: CurrentStockView[] = [];
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

    this.staticReportsDataService.getReportPreview('/stocks/current', params)
      .subscribe(
        (res: ReportInformation) => {
          // console.log('res.reportData');
          // console.log(res.reportData);

          res.reportData.forEach((rd: any) => {
            this.totalOrderCost += rd.lrcrTotalValue;
            this.totalSellingValue += rd.totalSellingValue;
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
