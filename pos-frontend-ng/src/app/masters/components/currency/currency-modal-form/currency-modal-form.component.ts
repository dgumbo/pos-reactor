import { Component, OnInit, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';
import { Currency } from 'shared/models/masters/currency';
import { CurrencyDataService } from 'app/masters/services/currency-data.service';

@Component({
  selector: 'app-currency-modal-form',
  templateUrl: './currency-modal-form.component.html',
  styleUrls: ['./currency-modal-form.component.scss']
})
export class CurrencyModalFormComponent implements OnInit {
  newForm = true;
  currencies: Currency[] = [];
  currency = <Currency>{};

  constructor(public dialogRef: MatDialogRef<CurrencyModalFormComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { currency: Currency, currencies: Currency[] },
  ) {
    this.newForm = data.currency.id ? false : true;
    this.currency = data.currency;
    this.currencies = data.currencies;
  }

  ngOnInit() {
    if (this.newForm) {
      this.currency.currencyRate = 1;
    }
  }

  returnCurrency() {
    this.dialogRef.close(this.currency);
  }
}

