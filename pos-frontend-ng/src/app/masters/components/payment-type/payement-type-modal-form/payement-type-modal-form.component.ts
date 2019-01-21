import { Component, OnInit, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material';
import { PaymentType } from 'app/masters/models/payment-type';

@Component({
  selector: 'app-payement-type-modal-form',
  templateUrl: './payement-type-modal-form.component.html',
  styleUrls: ['./payement-type-modal-form.component.scss']
})
export class PayementTypeModalFormComponent implements OnInit {
  newForm = true;
  currencies = [];

  constructor(public dialogRef: MatDialogRef<PayementTypeModalFormComponent>,
    @Inject(MAT_DIALOG_DATA) public paymentType: PaymentType) {
    this.newForm = paymentType.id ? false : true;
  }

  ngOnInit() {
  }

  returnPaymentType() {
    this.dialogRef.close(this.paymentType);
  }
}

