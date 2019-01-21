import { Injectable } from '@angular/core';
import { Product, ScheduleOfPrice } from 'app/shared/models';
import { Observable } from 'rxjs';
import { HttpParams, HttpClient } from '@angular/common/http';
import { DataRestService } from 'shared/services/data-rest.service';

@Injectable({
  providedIn: 'root'
})
export class ScheduleOfPriceDataService extends DataRestService {

  constructor(httpClient: HttpClient) {
    const resourcePath = '/api/schedule-of-prices';
    super(httpClient, resourcePath);
  }

  updateProductScheduleOfPrice(product: Product, scheduleOfPrice: ScheduleOfPrice): Observable<any> {
    const params = new HttpParams()
      .append('newPrice', scheduleOfPrice.price.toString())
      .append('currencyId', scheduleOfPrice.currency.id.toString());

    return this.restHttpClient.post(this.url + '/update-product-price', JSON.stringify(product), {
      headers: this.headers,
      params: params
    });
  }

  updateProductPrice(product: Product, newPrice: number, currencyId: number): Observable<any> {
    const params = new HttpParams()
      .append('newPrice', newPrice.toString())
      .append('currencyId', currencyId.toString());

    return this.restHttpClient.post(this.url + '/update-product-price', JSON.stringify(product), {
      headers: this.headers,
      params: params
    });
  }
  updateAllBondFromUsdPrice():  Observable<any> {
    return this.restHttpClient.post(this.url + '/update-all-bond-from-usd-price', JSON.stringify({}), {
      headers: this.headers,
      // params: params
    });
  }
  updateAllUsdFromBondPrice():  Observable<any> {
    return this.restHttpClient.post(this.url + '/update-all-usd-from-bond-price', JSON.stringify({}), {
      headers: this.headers,
      // params: params
    });
  }
}
