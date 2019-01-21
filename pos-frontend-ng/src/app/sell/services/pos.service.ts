import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { Product, CartItem, Cart, PaymentDetail } from 'app/shared/models';

@Injectable()
export class PosService {
    private saleWithDateCount = 0;

    private cart = <Cart>{ items: [], totalAmount: 0, itemsCount: 0, payment: <PaymentDetail>{}, totalEcocashAmount: 0 };

    private ticketSource = new BehaviorSubject<CartItem[]>(this.cart.items);
    private cartTotalSource = new BehaviorSubject<number>(this.cart.totalAmount);
    private cartTotalEcocashSource = new BehaviorSubject<number>(this.cart.totalEcocashAmount);
    private cartNumSource = new BehaviorSubject<number>(this.cart.itemsCount);

    private saleWithDateCountSource = new BehaviorSubject<number>(this.saleWithDateCount);

    currentTicket = this.ticketSource.asObservable();
    currentTotal = this.cartTotalSource.asObservable();
    currentEcocashTotal = this.cartTotalEcocashSource.asObservable();
    currentCartNum = this.cartNumSource.asObservable();

    currentSaleWithDateCount = this.saleWithDateCountSource.asObservable();

    constructor() { }

    changeTicket(cartItems: CartItem[]) {
        this.ticketSource.next(cartItems);
    }

    updateTotal(total: number) {
        this.cartTotalSource.next(total);
    }

    updateEcocashTotal(totalEcocash: number) {
        this.cartTotalEcocashSource.next(totalEcocash);
    }

    updateNumItems(num: number) {
        this.cartNumSource.next(num);
    }

    updateSaleWithDateCount(num: number) {
        this.saleWithDateCountSource.next(num);
    }
}
