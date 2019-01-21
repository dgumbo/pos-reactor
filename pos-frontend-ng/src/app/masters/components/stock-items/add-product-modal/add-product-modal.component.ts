import { Component, OnInit, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material';
import { Product, ScheduleOfPrice, ProductCategory } from 'shared/models';
import { ChargeType } from 'shared/models/enums/charge-type';
import { Currency } from 'shared/models/masters/currency';
import { CurrentStock } from 'shared/models/current-stock';

@Component({
  selector: 'app-add-product-modal',
  templateUrl: './add-product-modal.component.html',
  styleUrls: ['./add-product-modal.component.scss']
})
export class AddProductModalComponent implements OnInit {

  newForm = true;
  product = <Product>{};
  price = <{ currency: Currency, price: number }>{};
  currencies: Currency[] = [];
  productCategories: ProductCategory[] = [];

  constructor(public dialogRef: MatDialogRef<AddProductModalComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { product: Product, currencies: Currency[], productCategories: ProductCategory[] },
  ) {
    this.newForm = data.product.id ? false : true;
    this.currencies = data.currencies;
    this.productCategories = data.productCategories;
    this.product = this.data.product;

    this.product.currentStock = this.product.currentStock ? this.product.currentStock : <CurrentStock>{};
    this.setProductPrice(this.newForm, this.product, this.currencies, this.productCategories);
  }

  ngOnInit() {
  }

  setProductPrice(newForm: boolean, product: Product, currencies: Currency[], productCategories: ProductCategory[]) {
    if (!newForm) {
      const bondPrice = this.product.bondPrice;

      this.price.currency = currencies.find(c => c.name.toLowerCase().includes('bond'));
      this.price.price = bondPrice; 

      this.product.productCategory = productCategories.find(pc => pc.name === product.productCategory.name);
    }
  }

  returnProduct() {
    const scheduleOfPrice = <ScheduleOfPrice>{};
    scheduleOfPrice.additionalCharge = 0;
    scheduleOfPrice.chargeType = ChargeType.PRIMARY;
    scheduleOfPrice.currency = this.price.currency;
    scheduleOfPrice.price = this.price.price;
    this.product.scheduleOfPrices = [scheduleOfPrice];

    this.dialogRef.close(this.product);
  }

  onCancel() {
    this.dialogRef.close(null);
  }
}
