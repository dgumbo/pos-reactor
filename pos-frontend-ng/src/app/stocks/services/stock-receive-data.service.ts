import { Injectable } from '@angular/core';
import { DataRestService } from 'shared/services/data-rest.service';
import { HttpClient, HttpParams } from '@angular/common/http';
import { StockReceive } from 'app/shared/models/stock-receive';
import { Observable } from 'rxjs';

@Injectable({
    providedIn: 'root'
})
export class StockReceiveDataService extends DataRestService {

    constructor(httpClient: HttpClient) {
        const resourcePath = '/api/stocks/receive';
        super(httpClient, resourcePath);
    }

    createStockReceive(stockReceipt: StockReceive): Observable<any> {
        return this.restHttpClient.put(this.url + '/receive', JSON.stringify(stockReceipt), {
            headers: this.headers
        });
    }

    getAllAvailaleStock(): Observable<any> {
        return this.restHttpClient.get(this.url + '/available-stock');
    }

    getAllStockReceives(): Observable<any> {
        //    console.log("pano!!") ;
        return this.restHttpClient.get(this.url);
    }
}
