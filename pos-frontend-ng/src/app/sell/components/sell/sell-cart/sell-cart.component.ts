import { Component, OnInit, Input, EventEmitter, Output, ElementRef, ViewChild, HostListener } from '@angular/core';
import { CartItem, Cart, PaymentDetail, Sell } from 'app/shared/models';
import { ClockService } from 'shared/services/clock.service';
import { MatDialog } from '@angular/material';
import { Currency } from 'shared/models/masters/currency';
import { CurrencyDataService } from 'app/masters/services/currency-data.service';
import { PaymentType } from 'app/masters/models/payment-type';
import { PaymentTypeService } from 'app/masters/services/payment-type.service';
import { Router } from '@angular/router';
import { SellPrintService, PosService, SellService } from 'app/sell/services';

import { Store, select } from '@ngrx/store';
import * as fromRoot from 'app/app.reducer';
import * as  SellActions from 'app/sell/actions/sell-ui.action';
import { Observable, BehaviorSubject, Subject, merge } from 'rxjs';
import { PaymentModalComponent } from '../../payment/payment-modal/payment-modal.component';
import { async } from 'q';
import { combineLatest } from 'rxjs/operators';

@Component({
  selector: 'app-sell-cart',
  templateUrl: './sell-cart.component.html',
  styleUrls: ['./sell-cart.component.scss']
})
export class SellCartComponent implements OnInit {

  @ViewChild('checkoutButton') checkoutButton: ElementRef;
  @Input() isDateSell: boolean;

  currencies: Currency[] = [];
  ecocashCurrency: Currency = null;
  paymentTypes: PaymentType[] = [];

  enterCount = 0;

  isCartOpen$: Observable<boolean>;
  isProcessingPayment$: Observable<boolean>;
  isDialogOpen$: Observable<boolean>;
  isSubmittingSale$: Observable<boolean>;

  cart = <Cart>{ items: [], payment: <PaymentDetail>{} }; // itemsCount: 0, paidAmount: 0,  tenderedAmount: 0,
  // isDateSell = false;

  constructor(private posService: PosService,
    private cartService: SellService,
    private sellPrintService: SellPrintService,
    private clockService: ClockService,
    private currencyService: CurrencyDataService,
    private paymentTypeService: PaymentTypeService,
    public dialog: MatDialog,
    private router: Router,
    private store: Store<fromRoot.State>,
  ) { }

  // Sync with posService service on init
  ngOnInit() {
    // console.log('isDateSell : ', this.isDateSell) ;

    this.getCurrencies();
    this.getPaymentTypes();
    this.posService.currentTicket.subscribe(data => this.cart.items = data);
    this.posService.currentTotal.subscribe(total => this.cart.totalAmount = total);
    this.posService.currentEcocashTotal.subscribe(ecocashTotal => this.cart.totalEcocashAmount = ecocashTotal);
    this.posService.currentCartNum.subscribe(num => this.cart.itemsCount = num);

    this.clockService.getClock().subscribe(time => {
      if (!this.cart.dateSaleDate) {
        this.cart.dateSaleDate = time;
      } else if (!this.isDateSell) {
        this.cart.dateSaleDate = time;
      }
    });

    this.cart.dateSale = this.isDateSell;

    this.isCartOpen$ = this.store.pipe(select(fromRoot.getIsCartOpen));
    this.isProcessingPayment$ = this.store.pipe(select(fromRoot.getIsProcessingPayment));
    this.isDialogOpen$ = this.store.pipe(select(fromRoot.getIsDialogOpen));
    this.isSubmittingSale$ = this.store.pipe(select(fromRoot.getIsSubmittingSale));

    // this.store.pipe(select(fromRoot.getIsCartOpen)).subscribe((res: boolean) => this.isCartOpen = res);
    // this.store.pipe(select(fromRoot.getIsProcessingPayment)).subscribe((res: boolean) => this.isProcessingPayment = res);
    // this.store.pipe(select(fromRoot.getIsDialogOpen)).subscribe((res: boolean) => this.isDialogOpen = res);
    // this.store.pipe(select(fromRoot.getIsSubmittingSale)).subscribe((res: boolean) => this.isSubmittingSale = res);
  }

  // Increase quantity by one
  setFocusOnSubmitPayment() {
    this.checkoutButton.nativeElement.focus();
  }

  // Increase quantity by one
  refreshCartTotals() {
    this.syncTicket();
    this.calculateTotals();
  }

  getCurrencies() {
    this.currencyService.getAll()
      .subscribe((res: Currency[]) => {
        this.currencies = res;

        /* Checking for default currency */
        let hasDefault = 0;
        this.currencies.forEach(cur => {
          if (cur.defaultCurrency) {
            hasDefault += 1;
          }

          if (cur.name.trim().toLowerCase().includes('ecocash')) {
            this.ecocashCurrency = cur;
          }
        });

        if (res.length === 0) {
          alert('Please create currency');
          this.router.navigate(['/', 'masters', 'currency']);
        } else if (hasDefault === 0) {
          alert('Please select default currency');
          this.router.navigate(['/', 'masters', 'currency']);
        }
        /* End Checking for default currency */
      });
  }

  getPaymentTypes() {
    this.paymentTypeService.getAll()
      .subscribe((res: PaymentType[]) => {
        this.paymentTypes = res;

        /* End Checking for default payment type */
        let hasDefault = 0;
        this.paymentTypes.forEach(cur => {
          if (cur.defaultPaymentType) {
            hasDefault += 1;
          }
        });

        if (res.length === 0) {
          alert('Please create payment type');
          this.router.navigate(['/', 'masters', 'payment-type']);
        } else if (hasDefault === 0) {
          alert('Please select default payment type');
          this.router.navigate(['/', 'masters', 'payment-type']);
        }
        /* End Checking for default payment type */
      });
  }

  // Increase quantity by one
  addOneToItemQuantity(item: CartItem) {
    // If the item already exists, add 1 to quantity
    const cartIndex: number = this.cart.items.findIndex(p => p.product.id === item.product.id);
    if (cartIndex >= 0) {
      this.cart.items[cartIndex].quantity += 1;
    } else {
      const cartItem = <CartItem>{ product: item.product, quantity: 1, stock: null, unit_price: null, batchNumber: null };
      this.cart.items.push(cartItem);
    }
    this.refreshCartTotals();
  }

  // Reduce quantity by one
  subtractOneFromItemQuantity(cartItem: CartItem) {
    // Check if last item, if so, use remove method
    const cartIndex: number = this.cart.items.findIndex(p => p.product.id === cartItem.product.id);
    if (cartIndex >= 0) {
      if (this.cart.items[cartIndex].quantity === 1) {
        this.removeItem(cartItem);
      } else {
        this.cart.items[cartIndex].quantity = this.cart.items[cartIndex].quantity - 1;
      }
    }
    this.refreshCartTotals();
  }

  // Remove item from ticket
  removeItem(cartItem: CartItem) {
    // Check if item is in array
    const cartIndex: number = this.cart.items.findIndex(p => p.product.id === cartItem.product.id);
    if (cartIndex >= 0) {
      this.cart.items[cartIndex].quantity = 1;
      this.cart.items.splice(cartIndex, 1);
    }
    this.refreshCartTotals();
  }

  // Calculate cart total
  calculateTotals() {
    let total = 0;
    let cartitems = 0;
    // Multiply item price by item quantity, add to total
    this.cart.items.forEach((item: CartItem) => {
      total += (item.product.bondPrice * item.quantity);
      cartitems += item.quantity;
    });
    this.cart.totalAmount = total;
    const bondRate = this.ecocashCurrency.bondRate;
    const ecoRate = this.ecocashCurrency.currencyRate;
    this.cart.totalEcocashAmount = total * ecoRate / bondRate;
    this.cart.itemsCount = cartitems;
    // Sync total with posService service.
    this.posService.updateNumItems(this.cart.itemsCount);
    this.posService.updateTotal(this.cart.totalAmount);
    this.posService.updateEcocashTotal(this.cart.totalEcocashAmount);
  }

  // Remove all items from cart
  clearCart() {
    // Reduce back to initial quantity (1 vs 0 for re-add)
    this.cart.items.forEach((item: CartItem) => {
      item.quantity = 1;
    });
    // Empty local ticket variable then sync
    this.cart.items = [];
    this.cart.itemsCount = 0;
    this.refreshCartTotals();
  }

  syncTicket() {
    this.posService.changeTicket(this.cart.items);
  }

  checkout() {
    const isSubmittingSale = new BehaviorSubject(false);
    this.isSubmittingSale$.subscribe(isSubmittingSale);

    const isProcessingPayment = new BehaviorSubject(false);
    this.isProcessingPayment$.subscribe(isProcessingPayment);

    const isDialogOpen = new BehaviorSubject(false);
    this.isDialogOpen$.subscribe(isDialogOpen);


    if (!isSubmittingSale.value
      && !isProcessingPayment.value
      && !isDialogOpen.value) {
      if (this.cart.items.length > 0) {
        const dialogRef = this.dialog.open(PaymentModalComponent, {
          width: '800px',
          data: { payableAmount: this.cart.totalAmount, currencies: this.currencies, paymentTypes: this.paymentTypes }
        });

        this.store.dispatch(new SellActions.ProccessPayment);
        dialogRef.afterClosed().subscribe(
          (payment: PaymentDetail) => {
            this.store.dispatch(new SellActions.PaymentCompleted);
            // this.posService.updateProcessingPaymentStatus(false);

            if (payment.tenderedAmount > 0) {
              this.cart.payment = payment;

              if (+payment.tenderedAmount.toFixed(2) >= +this.cart.totalAmount.toFixed(2)) {
                this.cart.totalAmount = +this.cart.totalAmount.toFixed();
                this.submitSale(this.cart);
              }
            }
          },
          (error: Error) => {
            this.store.dispatch(new SellActions.PaymentCompleted);
            // this.posService.updateProcessingPaymentStatus(false);
          },
        );
      }
    }
  }

  submitSale(cart: Cart): any {
    this.store.dispatch(new SellActions.StartSellSubmition);
    this.cartService.processSell(this.cart)
      .subscribe(
        (res: Sell) => {
          this.store.dispatch(new SellActions.SellSubmitionComplete);
          this.clearCart();
          this.sellPrintService.printSellReciept(res, this.isDateSell);
        },
        (error: Error) => {
          this.store.dispatch(new SellActions.SellSubmitionComplete);
          console.log(error.message);
        }
      );
  }

  @HostListener('window:keyup', ['$event', 'undefined'])
  onKeyup(event: KeyboardEvent) {
    // console.log(event.key);

    if (event.key === 'Enter') {
      this.enterCount += 1;

      if (this.enterCount >= 2) {
        this.enterCount = 0;
        this.setFocusOnSubmitPayment();
      }
    }
  }

  @HostListener('window:click', ['$event', 'undefined'])
  onClick(event: MouseEvent) {
    this.enterCount = 0;
  }
}


