import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Sell } from 'shared/models';
import { PosService } from './pos.service';

@Injectable()
export class SellPrintService {
  isPrinting = false;
  isDateSell = false;
  dateSaleCount: number;

  constructor(private router: Router,
    private ticketSync: PosService) {
    this.ticketSync.currentSaleWithDateCount
      .subscribe(num => this.dateSaleCount = num);
  }

  printSellReciept(sell: Sell, isDateSell: boolean) {
    // console.log('here printing - printSellReciept');
    // console.log(sell);
    this.isPrinting = true;
    this.isDateSell = isDateSell;

    this.router.navigate(['sell',
      {
        outlets: {
          'print': ['print', 'invoice', sell.id]
        }
      }]);
  }

  onDataReady() {
    setTimeout(() => {
      window.print();
      this.isPrinting = false;
      // console.log('printed!!!');
      if (!this.isDateSell || this.dateSaleCount >= 5) {
        if (this.dateSaleCount >= 50) {
          console.log('Reached 50 Sales With Date Set');
          this.ticketSync.updateSaleWithDateCount(0);
        }
        this.router.navigate(['/sell']);
      } else {
        // console.log('/sell/date-sell');
        this.router.navigateByUrl('/sell/date-sell');
      }
    });
  }
}
