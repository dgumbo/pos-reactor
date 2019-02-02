import { Injectable } from '@angular/core';
import { DataRestService } from 'shared/services/data-rest.service';
import { StockTake } from 'shared/models/stocks/stock-take';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class StockTakeService extends DataRestService {

  constructor(httpClient: HttpClient) {
    const resourcePath = '/api/stocks/take';
    super(httpClient, resourcePath);
  }

  partialSaveStockTake(stockTake: StockTake): Observable<any> {
    return this.restHttpClient.put(this.url + '/partial-save-stock-take', JSON.stringify(stockTake), {
      headers: this.headers
    });
  }

  finalizeStockTake(stockTake: StockTake): Observable<any> {
    return this.restHttpClient.post(this.url + '/finalize-stock-take', JSON.stringify(stockTake), {
      headers: this.headers
    });
  }

  checkPendingStockTake(): Observable<any> {
    return this.restHttpClient.get(this.url + '/check-pending-stock-take');
  }

  getAllStockItems(): Observable<any> {
    return this.restHttpClient.get(this.url + '/all-stock-items');
  }

  getPendingStockTake(): any {
    return this.restHttpClient.get(this.url + '/pending-stock-take');
  }
}
