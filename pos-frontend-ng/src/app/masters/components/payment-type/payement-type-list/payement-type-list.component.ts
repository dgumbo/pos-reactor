import { Component, OnInit } from '@angular/core';
import { PaymentType } from 'app/masters/models/payment-type';
import { MatTableDataSource, MatDialog, MatDialogConfig } from '@angular/material';
import { PaymentTypeService } from 'app/masters/services/payment-type.service';
import { PayementTypeModalFormComponent } from '../payement-type-modal-form/payement-type-modal-form.component';

@Component({
  selector: 'app-payement-type-list',
  templateUrl: './payement-type-list.component.html',
  styleUrls: ['./payement-type-list.component.scss']
})
export class PayementTypeListComponent implements OnInit {

  paymentTypes: PaymentType[] = [];

  displayedColumns: string[] = ['action', 'name', 'activeStatus', 'modificationTime', 'defaultPaymentType'];
  dataSource = new MatTableDataSource<PaymentType>();

  constructor(private paymentTypeService: PaymentTypeService,
    public dialog: MatDialog) { }

  ngOnInit() {
    this.getPaymentTypes();
  }

  getPaymentTypes() {
    this.paymentTypeService.getAll()
      .subscribe((res: PaymentType[]) => {
        this.paymentTypes = res;
        this.dataSource.data = this.paymentTypes;
      });
  }

  addNewPaymentType() {
    this.openDialog(<PaymentType>{});
  }

  editPaymentType(exchangeRate: PaymentType) {
    this.openDialog(exchangeRate);
  }

  openDialog(exchangeRate: PaymentType) {
    const dialogConfig = new MatDialogConfig();
    dialogConfig.data = exchangeRate;
    dialogConfig.width = '900px';

    const dialogRef = this.dialog.open(PayementTypeModalFormComponent, dialogConfig);

    dialogRef.afterClosed().subscribe((diagRes: PaymentType) => {
      if (diagRes && diagRes.name) {
        this.paymentTypeService.create(diagRes)
          .subscribe((res: PaymentType) => {
            if (res.id && res.id > 0) {
              this.getPaymentTypes();
            }
          });
      }
    });
  }

}
