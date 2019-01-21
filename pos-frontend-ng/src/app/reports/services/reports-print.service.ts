import { Injectable } from '@angular/core';
import { Router } from '@angular/router';

@Injectable()
export class ReportsPrintService {
  isPrinting = false;
  isDateSell = false;
  dateSaleCount: number;

  constructor(private router: Router, ) {
  }

  printStockOutRecieptReport() {
    // console.log('here printing - printSellReciept');
    // console.log(sell);
    this.isPrinting = true;

    this.router.navigate(['reports',
      {
        outlets: {
          'print': ['print', 're-order']
        }
      }]);
  }

  onDataReady() {
    setTimeout(() => {
      window.print();
      this.isPrinting = false;
      console.log('printed!!!');
      this.router.navigate(['/reports']);
    });
  }
}
