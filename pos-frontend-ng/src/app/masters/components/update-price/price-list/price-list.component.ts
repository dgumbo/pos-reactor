import { Component, OnInit } from '@angular/core';
import { ScheduleOfPrice, Product, CurrentStock } from 'shared/models';
import { MatDialog, MatTableDataSource, MatDialogConfig, MatSnackBar } from '@angular/material';
import { CurrentStockDataService } from 'app/stocks/services';
import { ProductDataService } from 'shared/services';
import { AddProductModalComponent } from '../../stock-items/add-product-modal/add-product-modal.component';
import { Currency } from 'shared/models/masters/currency';
import { CurrencyDataService } from 'app/masters/services/currency-data.service';
import { PriceUpdateModalFormComponent } from '../price-update-modal-form/price-update-modal-form.component';
import { ScheduleOfPriceDataService } from 'admin/services/schedule-of-price-data.service';

@Component({
  selector: 'app-price-list',
  templateUrl: './price-list.component.html',
  styleUrls: ['./price-list.component.scss']
})
export class PriceListComponent implements OnInit {

  products: Product[] = [];
  unFilteredProductsHolder: Product[] = [];

  displayedColumns: string[] = ['action', 'name', 'bondPrice', 'ecocashPrice', 'usdPrice', 'currentStock'];
  dataSource = new MatTableDataSource<Product>();

  currencies: Currency[] = [];
  bondCurrency = <Currency>{};
  usdCurrency = <Currency>{};
  ecocashCurrency = <Currency>{};

  constructor(public dialog: MatDialog,
    private currentStockService: CurrentStockDataService,
    private currencyService: CurrencyDataService,
    private scheduleOfPriceService: ScheduleOfPriceDataService,
    private snackbar: MatSnackBar, ) {
    this.getCurrencies();
  }

  ngOnInit() {
    // console.log('yahoo!');
    this.getProducts();
  }

  getProducts() {
    this.currentStockService.getAllAvailaleStock()
      .subscribe((res: Product[]) => {
        // console.log(res);
        this.products = res;
        this.unFilteredProductsHolder = this.products;
        this.dataSource.data = this.products;
      });
  }

  getCurrencies() {
    this.currencyService.getAll()
      .subscribe((res: Currency[]) => {
        const bondCurrencyName = 'Bond';
        this.bondCurrency = res.find(c => c.name.trim().toLocaleLowerCase() === bondCurrencyName.trim().toLocaleLowerCase());
        const usdCurrencyName = 'USD';
        this.usdCurrency = res.find(c => c.name.trim().toLocaleLowerCase() === usdCurrencyName.trim().toLocaleLowerCase());
        const ecocashCurrencyName = 'Ecocash';
        this.ecocashCurrency = res.find(c => c.name.trim().toLocaleLowerCase() === ecocashCurrencyName.trim().toLocaleLowerCase());

        this.currencies = res;
      });
  }

  getBondPrice(prices: ScheduleOfPrice[]): ScheduleOfPrice {
    return this.getScheduleOfPrice(prices, this.bondCurrency);
  }

  getUsdPrice(prices: ScheduleOfPrice[]): ScheduleOfPrice {
    return this.getScheduleOfPrice(prices, this.usdCurrency);
  }

  getEcocashPrice(prices: ScheduleOfPrice[]): ScheduleOfPrice {
    return this.getScheduleOfPrice(prices, this.ecocashCurrency);
  }

  private getScheduleOfPrice(prices: ScheduleOfPrice[], currency: Currency) {
    let price = <ScheduleOfPrice>{};

    if (currency && currency.name && prices) {
      price = prices.find(p => p.currency.name === currency.name);
    }
    price = price ? price : <ScheduleOfPrice>{ currency: currency, price: 0 };

    return price;
  }

  editPrice(product: Product, scheduleOfPrice: ScheduleOfPrice) {
    this.openDialog(product, scheduleOfPrice);
  }

  openDialog(product: Product, scheduleOfPrice: ScheduleOfPrice) {
    this.products = this.unFilteredProductsHolder;
    this.dataSource.data = this.products;

    const dialogConfig = new MatDialogConfig();
    dialogConfig.data = { product: product, currencies: this.currencies, scheduleOfPrice: scheduleOfPrice };
    dialogConfig.width = '900px';

    const dialogRef = this.dialog.open(PriceUpdateModalFormComponent, dialogConfig);

    dialogRef.afterClosed().subscribe((diagRes: { product: Product, scheduleOfPrice: ScheduleOfPrice }) => {

      this.scheduleOfPriceService.updateProductScheduleOfPrice(diagRes.product, diagRes.scheduleOfPrice)
        .subscribe((res: Product) => {
          // console.log(res);
          const updatedProduct = this.products.find(p => p.id === res.id);

          const index = this.products.findIndex(ger => ger.name === res.name);
          if (index >= 0) {
            this.products.splice(index, 1, res);
          } else {
            this.products.push(res);
          }

          this.unFilteredProductsHolder = this.products;
          this.dataSource.data = this.products;
        });
    });
  }

  updatePrice(product: Product, scheduleOfPrice: ScheduleOfPrice) {

  }

  onFilterKeyup(filter: String) {
    // console.log(filter);
    if (filter !== 'Enter' && filter !== 'Tab' && filter !== 'Escape' && filter !== 'Control'
      && filter !== 'Backspace' && filter !== 'CapsLock' && filter !== 'Alt' && filter !== 'PageUp'
      && filter !== 'PageDown' && filter !== 'Shift' && filter !== 'CapsLock' && filter !== 'Delete'
      && filter !== 'NumLock' && filter !== 'Home' && filter !== 'End') {
      const filterTxt = filter.toLowerCase().trim().trimLeft().trimRight();

      this.products = this.unFilteredProductsHolder.filter(p => p.name.trim().toLocaleLowerCase().includes(filterTxt));
      this.dataSource.data = this.products;
    }
  }

  updateAllUsdFromBondPrice() {
    this.scheduleOfPriceService.updateAllUsdFromBondPrice()
      .subscribe((res: any) => {
        // console.log(res);
        this.snackbar.open('Operation Successiful', null, {
          duration: 1500
        });
        this.getProducts();
      });
  }

  updateAllBondFromUsdPrice() {
    this.scheduleOfPriceService.updateAllBondFromUsdPrice()
      .subscribe((res: any) => {
        // console.log(res);
        this.snackbar.open('Operation Successiful', null, {
          duration: 1500
        });
        this.getProducts();
      });
  }

}
