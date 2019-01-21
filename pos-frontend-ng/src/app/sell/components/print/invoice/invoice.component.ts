import { Component, OnInit, Input } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Sell } from 'shared/models/sell';
import { Receipt } from 'shared/models/sell/receipt';
import { SellPrintService, SellService } from 'app/sell/services';

@Component({
  selector: 'app-invoice',
  templateUrl: './invoice.component.html',
  styleUrls: ['./invoice.component.scss']
})
export class InvoiceComponent implements OnInit {
  sellId: number;
  date: Date;

  sell = <Sell>{ sellItems: [], receipt: <Receipt>{} };

  constructor(private route: ActivatedRoute, private printService: SellPrintService, private cartService: SellService) {

  }

  ngOnInit() {
    this.sellId = this.route.snapshot.params['sellId'];
    // console.log(this);
    // console.log('this.sellId', this.sellId);

    if (!this.sellId) {
      throw new TypeError('\'SellId\' is required');
    }

    this.cartService.get(this.sellId)
      .subscribe((res: Sell) => {
        this.sell = res;
        // this.date = res.sellDateTime;
        // console.log('this.sell');
        // console.log(this.sell);

        this.printService.onDataReady();
      });
  }

}
