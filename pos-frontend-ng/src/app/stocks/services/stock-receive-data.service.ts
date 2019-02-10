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

    finalizeStockReceive(stockReceipt: StockReceive): Observable<any> {
        return this.restHttpClient.post(this.url + '/finalize-stock-receive', JSON.stringify(stockReceipt), {
            headers: this.headers
        });
    }

    // createStockReceive(stockReceipt: StockReceive): Observable<any> {
    //     return this.restHttpClient.put(this.url + '/receive', JSON.stringify(stockReceipt), {
    //         headers: this.headers
    //     });
    // }

    partialSaveStockReceive(stockReceipt: StockReceive): Observable<any> {
        return this.restHttpClient.put(this.url + '/partial-save-stock-receive', JSON.stringify(stockReceipt), {
            headers: this.headers
        });
    }

    checkPendingStockReceive(): Observable<any> {
        return this.restHttpClient.get(this.url + '/check-pending-stock-receive');
    }

    getAllAvailaleStock(): Observable<any> {
        return this.restHttpClient.get(this.url + '/available-stock');
    }

    getAllStockReceives(): Observable<any> {
        //    console.log('pano!!') ;
        return this.restHttpClient.get(this.url);
    }

    getPendingStockReceive() {
        return this.restHttpClient.get(this.url + '/pending-stock-receive');
    }
}
