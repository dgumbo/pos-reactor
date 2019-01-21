import { Component, OnInit } from '@angular/core';
import { Upload, Product, ProductCategory, ScheduleOfPrice } from 'app/shared/models';
import { ProductDataService, ProductCategoryDataService } from 'shared/services';
import { ChargeType } from 'app/shared/models/enums/charge-type';
import { Currency } from 'shared/models/masters/currency';
import { CurrencyDataService } from 'app/masters/services/currency-data.service';
import { MatDialog, MatDialogConfig, MatTableDataSource } from '@angular/material';
import { AddProductModalComponent } from './add-product-modal/add-product-modal.component';
import { CurrentStock } from 'shared/models/current-stock';
import { CurrentStockDataService } from 'app/stocks/services/current-stock-data.service';

@Component({
  selector: 'app-products',
  templateUrl: './products.component.html',
  styleUrls: ['./products.component.scss']
})
export class ProductsComponent implements OnInit {

  products: Product[] = [];
  unFilteredProductsHolder: Product[] = [];
  productCategories: ProductCategory[] = [];

  currency: Currency;
  currencies: Currency[] = [];

  displayedColumns: string[] = ['action', 'name', 'price', 'currentStock'];
  dataSource = new MatTableDataSource<Product>();

  constructor(private productService: ProductDataService,
    private productCategoryService: ProductCategoryDataService,
    private currencyService: CurrencyDataService,
    private currentStockService: CurrentStockDataService,
    public dialog: MatDialog) { }

  ngOnInit() {
    // console.log('yahoo!');
    this.getProductCategories();
    this.getProducts();
    this.getCurrencies();
  }

  getProducts() {
    this.currentStockService.getAllAvailaleStock()
      .subscribe((res: Product[]) => {
        this.products = res;
        this.unFilteredProductsHolder = this.products;
        this.dataSource.data = this.products;
      });
  }

  getCurrencies() {
    this.currencyService.getAll()
      .subscribe((res: Currency[]) => this.currencies = res
      );
  }

  getProductCategories() {
    this.productCategoryService.getAll()
      .subscribe((res: ProductCategory[]) => this.productCategories = res
      );
  }

  addNewProduct() {
    this.openDialog(<Product>{});
  }

  editProduct(product: Product) {
    this.openDialog(product);
  }

  deleteItem(id) {
    const product = this.products.find(p => p.id === Number(id));

    if (product) {
      this.productService.delete(product)
        .subscribe((res: Product) => {
          console.log(res);
          const index = this.products.findIndex(p => p.id === res.id);
          this.products.splice(index, 1);
          this.dataSource.data = this.products;
        });
    }
  }

  openDialog(product: Product) {
    this.products = this.unFilteredProductsHolder;
    this.dataSource.data = this.products;

    const dialogConfig = new MatDialogConfig();
    dialogConfig.data = { product: product, currencies: this.currencies, productCategories: this.productCategories };
    dialogConfig.width = '900px';

    const dialogRef = this.dialog.open(AddProductModalComponent, dialogConfig);

    dialogRef.afterClosed().subscribe((diagRes: Product) => {
      if (diagRes && diagRes.name) {
        this.productService.create(diagRes)
          .subscribe((res: Product) => {

            if (diagRes.currentStock.quantity >= 1) {
              this.currentStockService
                .setProductCurrentStock(res.id, diagRes.currentStock.quantity)
                .subscribe((csRes: CurrentStock) => {
                  res.currentStock = csRes;
                });
            }

            const index = this.products.findIndex(ger => ger.name === res.name);
            if (index >= 0) {
              this.products.splice(index, 1);
            }

            this.products.push(res);
            this.unFilteredProductsHolder = this.products;
            this.dataSource.data = this.products;
          });
      }
    });
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
}
