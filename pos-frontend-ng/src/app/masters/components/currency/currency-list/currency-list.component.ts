import { Component, OnInit } from '@angular/core';
import { MatTableDataSource, MatDialog, MatDialogConfig } from '@angular/material';
import { CurrencyDataService } from 'app/masters/services/currency-data.service';
import { Currency } from 'shared/models/masters/currency';
import { CurrencyModalFormComponent } from '../currency-modal-form/currency-modal-form.component';

@Component({
  selector: 'app-currency-list',
  templateUrl: './currency-list.component.html',
  styleUrls: ['./currency-list.component.scss']
})
export class CurrencyListComponent implements OnInit {

  currencies: Currency[] = [];

  displayedColumns = ['action', 'name', 'symbol', 'currencyRate', 'bondRate', 'ecocashTransferRate', 'defaultCurrency', 'displayOnSummary'];
  dataSource = new MatTableDataSource<Currency>();

  constructor(private currencyService: CurrencyDataService,
    public dialog: MatDialog) { }

  ngOnInit() {
    this.getCurrencies();
  }

  getCurrencies() {
    this.currencyService.getAll()
      .subscribe((res: Currency[]) => {
        this.currencies = res;
        this.dataSource.data = this.currencies;
      });
  }

  addNewCurrency() {
    this.openDialog(<Currency>{});
  }

  editCurrency(currency: Currency) {
    this.openDialog(currency);
  }

  openDialog(currency: Currency) {
    const dialogConfig = new MatDialogConfig();
    dialogConfig.data = { currency: currency, currencies: this.currencies };
    dialogConfig.width = '900px';

    const dialogRef = this.dialog.open(CurrencyModalFormComponent, dialogConfig);

    dialogRef.afterClosed().subscribe((diagRes: Currency) => {
      if (diagRes && diagRes.name) {
        this.currencyService.create(diagRes)
          .subscribe((res: Currency) => {
            if (res.id && res.id > 0) {
              this.getCurrencies();
            }
          });
      }
    });
  }
}
