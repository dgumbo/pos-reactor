import { Component, OnInit } from '@angular/core';
import { trigger, state, transition, animate, style } from '@angular/animations';
import { Product } from 'app/shared/models';
import { MatTableDataSource } from '@angular/material';
import { ScheduleOfPriceDataService } from 'admin/services/schedule-of-price-data.service';
import { Currency } from 'shared/models/masters/currency';
import { CurrencyDataService } from 'app/masters/services/currency-data.service';
import { CurrentStockDataService } from 'app/stocks/services/current-stock-data.service';

@Component({
  selector: 'app-price-list',
  templateUrl: './price-list.component.html',
  styleUrls: ['./price-list.component.scss'],
  animations: [
    trigger('detailExpand', [
      state('collapsed', style({ height: '0px', minHeight: '0', display: 'none' })),
      state('expanded', style({ height: '*' })),
      transition('expanded <=> collapsed', animate('225ms cubic-bezier(0.4, 0.0, 0.2, 1)')),
    ]),
  ],
})
export class PriceListComponent implements OnInit {

  products: Product[];
  currencies: Currency[] = [];
  columnsToDisplay: string[] = ['name', 'bondPrice', 'lastReceiptCostRate', 'productCategory', 'currentStock'];
  dataSource = new MatTableDataSource<Product>();
  expandedProduct: Product;

  constructor(private currentStockService: CurrentStockDataService,
    private scheduleOfPriceService: ScheduleOfPriceDataService,
    private currencyService: CurrencyDataService) { }

  ngOnInit() {
    this.getProducts();
    this.getCurrencies();
  }

  getProducts(): any {
    this.dataSource.data = [];
    this.currentStockService.getAllAvailaleStock()
      .subscribe((res: Product[]) => {
        this.products = res;
        this.dataSource.data = this.products;
      });
  }

  getCurrencies(): any {
    this.dataSource.data = [];
    this.currencyService.getAll()
      .subscribe((res: Currency[]) => {
        this.currencies = res;
      });
  }

  updatePrice(updProd, newPrice, currencyId) {
    // console.log("id : ", id);
    // console.log("newPrice : ", newPrice);
    // console.log("currencyId : ", currencyId);

    const prod: Product = JSON.parse(updProd);
    const product = this.products.find(pp => pp.id === prod.id);
    if (product) {
      this.scheduleOfPriceService.updateProductPrice(product, Number(newPrice), currencyId)
        .subscribe((res: Product) => {
          // console.log(res);
          const updatedProduct = this.products.find(p => p.id === res.id);
          updatedProduct.bondPrice = res.bondPrice;
          this.expandedProduct = <Product>{};
        });
    }
  }

  onFilterKeyup(filter: String) {
    // console.log(filter);
    if (filter !== 'Enter' && filter !== 'Tab' && filter !== 'Escape' && filter !== 'Control'
      && filter !== 'Backspace' && filter !== 'CapsLock' && filter !== 'Alt' && filter !== 'PageUp'
      && filter !== 'PageDown' && filter !== 'Shift' && filter !== 'CapsLock' && filter !== 'Delete'
      && filter !== 'NumLock' && filter !== 'Home' && filter !== 'End') {

      const filterTxt = filter.toLowerCase().trim().trimLeft().trimRight();
      this.dataSource.data = this.products.filter(p => p.name.trim().toLocaleLowerCase().includes(filterTxt));
    }
  }

}
