import { Component, OnInit } from '@angular/core';
import { GlobalExchangeRate } from 'app/masters/models/global-exchange-rate';
import { MatTableDataSource, MatDialog, MatDialogConfig } from '@angular/material';
import { ExchangeRateModalFormComponent } from '../exchange-rate-modal-form/exchange-rate-modal-form.component';
import { ExchangeRatesService } from 'app/masters/services/exchange-rates.service';

@Component({
  selector: 'app-exchange-rates-list',
  templateUrl: './exchange-rates-list.component.html',
  styleUrls: ['./exchange-rates-list.component.scss']
})
export class ExchangeRatesListComponent implements OnInit {

  globalExchangeRates: GlobalExchangeRate[] = [];

  displayedColumns: string[] = ['action', 'currency', 'currencyRate', 'bondRate'];
  dataSource = new MatTableDataSource<GlobalExchangeRate>();

  constructor(private exchangeRatesService: ExchangeRatesService,
    public dialog: MatDialog) { }

  ngOnInit() {
    this.getSuppliers();
  }

  getSuppliers() {
    this.exchangeRatesService.getAll()
      .subscribe((res: GlobalExchangeRate[]) => {
        this.globalExchangeRates = res;
        this.dataSource.data = this.globalExchangeRates;
      });
  }

  addNewGlobalExchangeRate() {
    this.openDialog(<GlobalExchangeRate>{});
  }

  editGlobalExchangeRate(exchangeRate: GlobalExchangeRate) {
    this.openDialog(exchangeRate);
  }

  openDialog(exchangeRate: GlobalExchangeRate) {
    const dialogConfig = new MatDialogConfig();
    dialogConfig.data = exchangeRate;
    dialogConfig.width = '900px';

    const dialogRef = this.dialog.open(ExchangeRateModalFormComponent, dialogConfig);

    dialogRef.afterClosed().subscribe((diagRes: GlobalExchangeRate) => {
      if (diagRes && diagRes.currency) {
        this.exchangeRatesService.create(diagRes)
          .subscribe((res: GlobalExchangeRate) => {
            const index = this.globalExchangeRates.findIndex(ger => ger.currency === res.currency);
            if (index >= 0) {
              this.globalExchangeRates.splice(index, 1);
            }

            this.globalExchangeRates.push(res);
            this.dataSource.data = this.globalExchangeRates;
          });
      }
    });
  }

}
