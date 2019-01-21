import { Component, OnInit, Inject } from '@angular/core';
import { Product, CurrentStock, ScheduleOfPrice } from 'shared/models';
import { Currency } from 'shared/models/masters/currency';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material'; 

@Component({
  selector: 'app-price-update-modal-form',
  templateUrl: './price-update-modal-form.component.html',
  styleUrls: ['./price-update-modal-form.component.scss']
})
export class PriceUpdateModalFormComponent implements OnInit {

  newForm = true;
  product = <Product>{};
  price = <{ currency: Currency, price: number }>{};
  currencies: Currency[] = [];

  oldPrice = 0;
  scheduleOfPrice = <ScheduleOfPrice>{};

  constructor(public dialogRef: MatDialogRef<PriceUpdateModalFormComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { product: Product, scheduleOfPrice: ScheduleOfPrice, currencies: Currency[], },
  ) {
    this.scheduleOfPrice = data.scheduleOfPrice;
    this.oldPrice = data.scheduleOfPrice.price;

    this.newForm = data.product.id ? false : true;
    this.currencies = data.currencies;
    this.product = this.data.product;

    this.product.currentStock = this.product.currentStock ? this.product.currentStock : <CurrentStock>{};
  }

  ngOnInit() {
    this.scheduleOfPrice.currency = this.currencies.find(c => c.name === this.scheduleOfPrice.currency.name);
  }

  returnScheduleOfPrice() {
    this.dialogRef.close({ product: this.product, scheduleOfPrice: this.scheduleOfPrice });
  }

  onCancel() {
    this.dialogRef.close(null);
  }
}
