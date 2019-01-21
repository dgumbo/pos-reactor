import { Component, OnInit, Inject, ViewChild, ElementRef, HostListener } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA, MatDialog } from '@angular/material';
import { PaymentDetail } from 'app/shared/models';
import { Currency } from 'shared/models/masters/currency';
import { PaymentType } from 'app/masters/models/payment-type';
import { PaymentLineModalFormComponent } from '../payment-line-modal-form/payment-line-modal-form.component';
import { ReceiptItem } from 'shared/models/sell/receipt-item';

@Component({
    selector: 'app-payment-modal',
    templateUrl: './payment-modal.component.html',
    styleUrls: ['./payment-modal.component.scss']
})
export class PaymentModalComponent implements OnInit {

    paymentDetails = <PaymentDetail>{};
    currencies: Currency[] = [];
    paymentTypes: PaymentType[] = [];
    totalOwing = 0;
    diagOpen = false;

    ngOnInit(): void { }

    constructor(public dialogRef: MatDialogRef<PaymentModalComponent>,
        @Inject(MAT_DIALOG_DATA) private data: { payableAmount: number, currencies: Currency[], paymentTypes: PaymentType[] },
        public dialog: MatDialog) {
        this.paymentDetails = { payableAmount: this.data.payableAmount, tenderedAmount: 0, receiptItems: [] };
        this.currencies = this.data.currencies;
        this.paymentTypes = this.data.paymentTypes;

        this.totalOwing = +this.data.payableAmount.toFixed(2);
        this.paymentDetails.changeAmount = 0;
        this.addReceiptLine();
    }

    addReceiptLine() {
        const paymentOwing = +this.paymentDetails.payableAmount.toFixed(2) - +this.paymentDetails.tenderedAmount.toFixed(2);

        if (paymentOwing > 0) {
            this.diagOpen = true;
            const dialogRef = this.dialog.open(PaymentLineModalFormComponent, {
                width: '700px',
                // height: '600px',
                data: { paymentOwing: paymentOwing, currencies: this.currencies, paymentTypes: this.paymentTypes }
            });

            dialogRef.afterClosed().subscribe((diagRes: ReceiptItem) => {
                // console.log(diagRes);
                if (diagRes && diagRes.amount > 0 && diagRes.currency) {
                    this.paymentDetails.receiptItems.push(diagRes);
                    this.calculateTotalPaid();
                    this.diagOpen = false;
                }
            });
        }
    }

    calculateTotalPaid() {
        let totalPaid = 0;
        this.paymentDetails.receiptItems.forEach(ri => {
            totalPaid += +ri.convertedAmount.toFixed(2);
        });

        this.paymentDetails.tenderedAmount = totalPaid;
        this.totalOwing = +(this.paymentDetails.payableAmount - totalPaid).toFixed(2);
        this.paymentDetails.changeAmount = +(totalPaid - this.paymentDetails.payableAmount).toFixed(2);
    }

    onNoClick(): void {
        this.dialogRef.close(<PaymentDetail>{});
    }

    @HostListener('window:keyup.enter', ['$event', 'undefined'])
    onKeyup(enterEvent: KeyboardEvent) {
        if (this.diagOpen === false) {
            this.calculateTotalPaid();
            if (this.totalOwing > 0) {
                this.addReceiptLine();
            } else {
                this.returnPaymentDetails(this.paymentDetails);
            }
        }
    }

    returnPaymentDetails(paymentDetails) {
        paymentDetails.changeAmount = +paymentDetails.changeAmount.toFixed();
        paymentDetails.payableAmount = +paymentDetails.payableAmount.toFixed();
        this.dialogRef.close(paymentDetails);
    }
}
