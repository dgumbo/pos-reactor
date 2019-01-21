import { Component, OnInit, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';
import { GlobalExchangeRate } from 'app/masters/models/global-exchange-rate';
import { ExchangeRatesService } from 'app/masters/services/exchange-rates.service'; 

@Component({
  selector: 'app-exchange-rate-modal-form',
  templateUrl: './exchange-rate-modal-form.component.html',
  styleUrls: ['./exchange-rate-modal-form.component.scss']
})
export class ExchangeRateModalFormComponent implements OnInit {
  newForm = true;
  currencies = [];

  constructor(public dialogRef: MatDialogRef<ExchangeRateModalFormComponent>,
    private exchangeRatesService: ExchangeRatesService,
    @Inject(MAT_DIALOG_DATA) public globalExchangeRate: GlobalExchangeRate
  ) {
    this.newForm = globalExchangeRate.currency ? false : true;
    this.getCurrencies();
  }

  getCurrencies() {
    this.exchangeRatesService.getCurrencies()
      .subscribe((res: any[]) => {
        this.currencies = res;
      });
  }

  ngOnInit() {
  }

  ReturnExchangeRate() {
    this.dialogRef.close(this.globalExchangeRate);
  }
}

