import { Component, OnInit, ViewChild, ElementRef, AfterViewInit, HostListener, Output, EventEmitter } from '@angular/core';
import { Product, ProductCategory, CartItem, Cart, PaymentDetail } from 'app/shared/models';
import { MatTableDataSource, MatDialog } from '@angular/material';
import { Router } from '@angular/router';
import { CurrentStockDataService } from 'app/stocks/services/current-stock-data.service';
import { CurrencyDataService } from 'app/masters/services/currency-data.service';
import { Currency } from 'shared/models/masters/currency';
import { PosService } from 'app/sell/services';

import { Store, select } from '@ngrx/store';
import * as fromRoot from 'app/app.reducer';
import * as  SellActions from 'app/sell/actions/sell-ui.action';
import { StockItemNotFoundModalComponent } from '../../stock-item-not-found-modal/stock-item-not-found-modal.component';

@Component({
  selector: 'app-item-list',
  templateUrl: './item-list.component.html',
  styleUrls: ['./item-list.component.scss']
})
export class ItemListComponent implements OnInit, AfterViewInit {

  @ViewChild('search') searchField: ElementRef;
  @Output() setFocusOnPaymentSubmitButton = new EventEmitter<void>();

  // processingPaymentInput: Observable<boolean>;

  enterCount = 0;

  products: any[] = [];
  items: Product[] = [];
  productCategories: ProductCategory[] = [];

  dialogClosed = true;

  displayedColumns: string[] = ['action', 'stockItem', 'quantity', 'price', 'ecocash', 'productCategory', 'expiryDate'];
  dataSource = new MatTableDataSource<Product>();

  cart: Cart = { items: [], totalAmount: 0, itemsCount: 0, payment: <PaymentDetail>{}, totalEcocashAmount: 0 };

  ecocashCurrency: Currency = null;

  isCartOpen = false;
  isProcessingPayment = false;
  isDialogOpen = false;
  isSubmittingSale = false;

  constructor(private posService: PosService,
    private currentStockDataService: CurrentStockDataService,
    private router: Router,
    private store: Store<fromRoot.State>,
    private currencyService: CurrencyDataService,
    public dialog: MatDialog, ) {
    this.getAllAvailaleStock();
    this.getEcocashCurrency();
  }

  ngOnInit() {
    this.posService.currentTicket.subscribe(data => this.cart.items = data);
    this.posService.currentTotal.subscribe(total => this.cart.totalAmount = total);
    this.posService.currentCartNum.subscribe(num => this.cart.itemsCount);
    this.posService.currentEcocashTotal.subscribe(ecocashTotal => this.cart.totalEcocashAmount = ecocashTotal);

    this.store.pipe(select(fromRoot.getIsCartOpen)).subscribe((res: boolean) => this.isCartOpen = res);
    this.store.pipe(select(fromRoot.getIsProcessingPayment)).subscribe((res: boolean) => this.isProcessingPayment = res);
    this.store.pipe(select(fromRoot.getIsDialogOpen)).subscribe((res: boolean) => this.isDialogOpen = res);
    this.store.pipe(select(fromRoot.getIsSubmittingSale))
      .subscribe((res: boolean) => {
        if (res === false && this.isSubmittingSale === true) {
          this.getAllAvailaleStock();
        }
        this.isSubmittingSale = res;
      });
  }

  ngAfterViewInit(): void {
    this.searchField.nativeElement.focus();
  }

  getEcocashCurrency() {
    this.currencyService.getAll()
      .subscribe((res: Currency[]) => {
        res.forEach(cur => {
          if (cur.name.trim().toLowerCase().includes('ecocash')) {
            this.ecocashCurrency = cur;
          }
        });
      });
  }

  getAllAvailaleStock() {
    this.currentStockDataService.getAllAvailaleStock()
      .subscribe(
        (res: Product[]) => {
          // console.log(res);
          /* Checking for Stock Items */
          if (res.filter(p => p.bondPrice > 0).length === 0) {
            alert('Please set prices ');
            this.router.navigate(['/', 'stocks', 'prices']);
          } else if (res.filter(p => p.currentStock.quantity > 0).length === 0) {
            alert('Please add stock');
            this.router.navigate(['/', 'stocks', 'stock-receive']);
          }
          /* End Checking for Stock Items */

          this.products = res
            .filter(p => p.currentStock.quantity > 0)
            .filter(p => p.bondPrice > 0)
            .sort((a, b) => a.name < b.name ? 1 : -1);
          this.dataSource.data = this.products;
          this.extractProductCategories(this.products);
        },
        (error: Error) => console.log(error) ,
      );
  }

  addToCheck(item: Product) {
    // If the item already exists, add 1 to quantity
    const cartIndex: number = this.cart.items.findIndex(p => p.product.id === item.id);
    if (cartIndex >= 0) {
      this.cart.items[cartIndex].quantity += 1;
    } else {
      const cartItem: CartItem = {
        product: item,
        quantity: 1,
        unit_price: item.bondPrice,
        stock: item.currentStock,
        batchNumber: 'BN'
      };
      this.cart.items.push(cartItem);
    }
    this.calculateTotal();
    this.searchField.nativeElement.value = '';
    this.searchField.nativeElement.focus();
    this.enterCount = 0;
  }

  // Calculate cart total
  calculateTotal() {
    let total = 0;
    let cartNum = 0;
    // Multiply item price by item quantity, add to total
    this.cart.items.forEach((item: CartItem) => {
      total += (item.product.bondPrice * item.quantity);
      cartNum += item.quantity;
    });
    this.cart.totalAmount = total;
    this.cart.itemsCount = cartNum;
    this.posService.updateNumItems(this.cart.itemsCount);
    this.posService.updateTotal(this.cart.totalAmount);

    const bondRate = this.ecocashCurrency.bondRate;
    const ecoRate = this.ecocashCurrency.currencyRate;
    this.cart.totalEcocashAmount = total * ecoRate / bondRate;
    // Sync total with posService service.
    this.posService.updateNumItems(this.cart.itemsCount);
    this.posService.updateTotal(this.cart.totalAmount);
    this.posService.updateEcocashTotal(this.cart.totalEcocashAmount);
  }

  syncTicket() {
    this.posService.changeTicket(this.cart.items);
  }

  getProductInCategory(name: string): Product[] {
    return this.products
      .filter((p: Product) => p.productCategory.name === name && p.bondPrice > 0 && p.currentStock && p.currentStock.quantity > 0);
  }

  extractProductCategories(products: Product[]): void {
    products
      .filter(p => p.bondPrice > 0 && p.currentStock && p.currentStock.quantity)
      .forEach(
        prod => {
          if (!this.productCategories.find(pc => pc.id === prod.productCategory.id)) {
            this.productCategories.push(prod.productCategory);
          }
        }
      );
  }

  filterProducts(element, event: KeyboardEvent) {
    // console.log(this.products) ;
    const searchStr = element.value.toLowerCase().trim();
    const filteredProducts = <Product[]>(<Product[]>(
      this.products
        .filter(p =>
          p.name.toLowerCase().trim().includes(searchStr)
          ||
          (p.barcode && p.barcode.trim().includes(searchStr))
        )
    ));
    this.dataSource.data = filteredProducts;

    if (event.key === 'Enter') {
      if (searchStr.length === 0) {
        if (this.enterCount >= 1) {
          this.setFocusOnPaymentSubmitButton.emit();
          this.enterCount = 0;
        } else if (this.cart.itemsCount >= 1) {
          this.enterCount += 1;
        } else {
          this.enterCount = 0;
        }
      } else if (searchStr.length > 0) {
        if (filteredProducts.length >= 1) {
          this.addToCheck(filteredProducts[0]);
          this.dataSource.data = this.products;
        } else {
          this.showItemNotFoundDialog(element.value);
        }
        this.enterCount = 0;
        element.value = '';
        this.filterProducts(element, event);
      }
    }
  }

  showItemNotFoundDialog(searchStr: String): void {
    this.dialogClosed = false;
    this.enterCount = 0;

    const dialogRef = this.dialog.open(StockItemNotFoundModalComponent, {
      data: searchStr
    });

    // this.proccessingPayment = true;
    this.store.dispatch(new SellActions.OpenModalDialogue);
    dialogRef.afterClosed()
      .subscribe(() => {
        this.store.dispatch(new SellActions.CloseModalDialogue);
        this.enterCount = 0;
        this.searchField.nativeElement.value = '';
        this.searchField.nativeElement.focus();
      });
  }

  @HostListener('window:keyup', ['$event', 'undefined'])
  onKeyup(event: KeyboardEvent) {
    // console.log(event.key);
    console.log('proccessingSale : ', this.isSubmittingSale);
    console.log('proccessingPayment : ', this.isProcessingPayment);
    console.log('isDialogOpen : ', this.isDialogOpen);
    if (!this.isSubmittingSale && !this.isProcessingPayment && !this.isDialogOpen) {
      const searchTxtboxFocused = document.activeElement === this.searchField.nativeElement;
      if (!searchTxtboxFocused) {
        if (event.key !== 'Enter' && event.key !== 'Tab' && event.key !== 'Escape' && event.key !== 'Control'
          && event.key !== 'Backspace' && event.key !== 'CapsLock' && event.key !== 'Alt' && event.key !== 'PageUp'
          && event.key !== 'PageDown' && event.key !== 'Shift' && event.key !== 'CapsLock' && event.key !== 'Delete'
          && event.key !== 'NumLock' && event.key !== 'Home' && event.key !== 'End') {

          this.searchField.nativeElement.value += event.key;
          this.searchField.nativeElement.focus();
        }
        this.filterProducts(this.searchField.nativeElement, event);
      }
    }
  }
}
