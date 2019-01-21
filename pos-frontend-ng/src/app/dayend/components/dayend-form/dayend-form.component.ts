import { Component, OnInit } from '@angular/core';
import { PaymentTypeService } from 'app/masters/services/payment-type.service';
import { DayendDataService } from 'app/dayend/services/dayend-data.service';
import { MatDialog, MatDialogConfig, MatTableDataSource } from '@angular/material';
import { PaymentType } from 'app/masters/models/payment-type';
import { Dayend } from 'shared/models/dayend/dayend';
import { DayendModalFormComponent } from '../dayend-modal-form/dayend-modal-form.component';
import { DayendItem } from 'shared/models/dayend/dayend-item';
import { CurrencyDataService } from 'app/masters/services/currency-data.service';
import { Currency } from 'shared/models/masters/currency';

@Component({
  selector: 'app-dayend-form',
  templateUrl: './dayend-form.component.html',
  styleUrls: ['./dayend-form.component.scss']
})
export class DayendFormComponent implements OnInit {
  currencies: string[] = [];
  paymentTypes: PaymentType[] = [];
  dayend = <Dayend>{ dayendItems: [] };
  dataSource = new MatTableDataSource<DayendItem>();

  constructor(private dayendService: DayendDataService,
    private paymentTypeService: PaymentTypeService,
    private currencyService: CurrencyDataService,
    public dialog: MatDialog) { }

  ngOnInit() {
    this.getCurrencies();
    this.getSuppliers();
  }

  getCurrencies() {
    this.currencyService.getAll()
      .subscribe((res: string[]) => {
        console.log('res:', res);
        this.currencies = res;
      });
  }

  getSuppliers() {
    this.paymentTypeService.getAll()
      .subscribe((res: PaymentType[]) => {
        this.paymentTypes = res;
      });
  }

  addNewDayend() {
    this.openDialog(<DayendItem>{ paymentType: <PaymentType>{}, currency: <Currency>{} });
  }

  editDayend(dayendItem: DayendItem) {
    this.openDialog(dayendItem);
  }

  openDialog(dayendItem: DayendItem) {
    const dialogConfig = new MatDialogConfig();
    dialogConfig.data = { currencies: this.currencies, paymentTypes: this.paymentTypes, dayendItem: dayendItem };
    dialogConfig.width = '900px';

    const dialogRef = this.dialog.open(DayendModalFormComponent, dialogConfig);

    dialogRef.afterClosed().subscribe((diagRes: DayendItem) => {
      // console.log('diagRes');
      // console.log(diagRes);
      this.dayend.dayendItems.push(diagRes);
      this.dataSource.data = this.dayend.dayendItems;
    });
  }

  disableSubmit(val) {

  }

}
