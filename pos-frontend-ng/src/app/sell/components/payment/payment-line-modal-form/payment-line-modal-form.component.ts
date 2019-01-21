import { Component, OnInit, Inject, ViewChild, ElementRef, HostListener } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';
import { Currency } from 'shared/models/masters/currency';
import { PaymentType } from 'app/masters/models/payment-type';
import { ReceiptItem } from 'shared/models/sell/receipt-item';

@Component({
  selector: 'app-payment-line-modal-form',
  templateUrl: './payment-line-modal-form.component.html',
  styleUrls: ['./payment-line-modal-form.component.scss']
})
export class PaymentLineModalFormComponent implements OnInit {
  @ViewChild('tenderedAmount') tenderedAmountField: ElementRef;
  @ViewChild('remarks') remarksField: ElementRef;

  paymentTypes: PaymentType[] = [];
  currencies: Currency[] = [];
  receiptItem = <ReceiptItem>{};
  changeAmount = 0;
  currencyPaymentOwing = 0;

  constructor(public dialogRef: MatDialogRef<PaymentLineModalFormComponent>,
    @Inject(MAT_DIALOG_DATA) private data: { paymentOwing: number, currencies: Currency[], paymentTypes: PaymentType[] }) {
    this.dialogRef.disableClose = true;

    this.currencies = this.data.currencies;
    this.paymentTypes = this.data.paymentTypes;

    this.currencies.forEach(cur => {
      if (cur.defaultCurrency) {
        this.receiptItem.currency = cur;
        const bondRate = this.receiptItem.currency.bondRate;
        const currencyRate = this.receiptItem.currency.currencyRate;
        this.currencyPaymentOwing = +(this.data.paymentOwing * currencyRate / bondRate).toFixed(2);
      }
    });

    this.paymentTypes.forEach(pt => {
      if (pt.defaultPaymentType) {
        this.receiptItem.paymentType = pt;
      }
    });
  }

  ngOnInit() {
    this.tenderedAmountField.nativeElement.focus();
    this.tenderedAmountField.nativeElement.select();
  }

  submitReceiptItem(receiptItem: ReceiptItem) {
    // console.log('receipt Item');
    // console.log(receiptItem);
    this.dialogRef.close(receiptItem);
  }

  onTypePaidAmount() {
    this.receiptItem.amount = this.tenderedAmountField.nativeElement.value;
    let convertedAmount = 0;
    if (this.receiptItem.currency) {
      const bondRate = this.receiptItem.currency.bondRate;
      const currencyRate = this.receiptItem.currency.currencyRate;

      this.currencyPaymentOwing = +(this.data.paymentOwing * currencyRate / bondRate).toFixed(2);

      convertedAmount = +(this.receiptItem.amount * bondRate / currencyRate).toFixed(2);
    }

    this.receiptItem.convertedAmount = convertedAmount;
    const change: number = +(convertedAmount - this.data.paymentOwing).toFixed(2);
    this.changeAmount = change > 0 ? +change.toFixed(2) : 0;
  }

  onCurrencyChange() {
    // console.log('papi') ;
    this.onTypePaidAmount();
  }

  onNoClick(): void {
    this.dialogRef.close(<ReceiptItem>{});
  }

  @HostListener('window:keyup.enter', ['$event', 'undefined'])
  onEnterEvent(enterEvent: KeyboardEvent) {
    const tenderedAmountTxtboxFocused = document.activeElement === this.tenderedAmountField.nativeElement;
    if (tenderedAmountTxtboxFocused) {
      if (+this.receiptItem.amount > 0) {
        this.submitReceiptItem(this.receiptItem);
      } else {
        this.tenderedAmountField.nativeElement.focus();
        this.tenderedAmountField.nativeElement.select();
      }
    }
  }

  @HostListener('window:keyup', ['$event', 'undefined'])
  onKeyup(event: KeyboardEvent) {
    const tenderedAmountTxtboxFocused = this.tenderedAmountField && document.activeElement === this.tenderedAmountField.nativeElement;
    const remarksTxtboxFocused = this.remarksField && document.activeElement === this.remarksField.nativeElement;
    // console.log(remarksTxtboxFocused);
    if (!tenderedAmountTxtboxFocused && !remarksTxtboxFocused && event.key !== 'Enter') {
      this.tenderedAmountField.nativeElement.value += event.key;
      this.tenderedAmountField.nativeElement.focus();
    }
  }
}
