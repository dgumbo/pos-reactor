import { Component, OnInit, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';
import { PaymentType } from 'app/masters/models/payment-type';
import { DayendItem } from 'shared/models/dayend/dayend-item';
import { Currency } from 'shared/models/masters/currency';

@Component({
  selector: 'app-dayend-modal-form',
  templateUrl: './dayend-modal-form.component.html',
  styleUrls: ['./dayend-modal-form.component.scss']
})
export class DayendModalFormComponent implements OnInit {

  newForm = true;
  paymentTypes: PaymentType[] = [];
  currencies: Currency[] = [];
  dayendItem: DayendItem = <DayendItem>{};

  constructor(public dialogRef: MatDialogRef<DayendModalFormComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { dayendItem: DayendItem, paymentTypes: PaymentType[], currencies: Currency[] }
  ) {
    this.newForm = data.dayendItem.id ? false : true;
    this.dayendItem = data.dayendItem;
    this.paymentTypes = data.paymentTypes;
    this.currencies = data.currencies;
  }

  ngOnInit() { }

  returnDayend() {
    // console.log('this.dayend');
    // console.log(this.dayendItem);
    this.dialogRef.close(this.dayendItem);
  }
}

