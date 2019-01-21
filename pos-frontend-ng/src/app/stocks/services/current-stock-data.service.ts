import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { DataRestService } from 'shared/services';

@Injectable({
    providedIn: 'root'
})
export class CurrentStockDataService extends DataRestService {

    constructor(httpClient: HttpClient) {
        const resourcePath = '/api/stocks/current';
        super(httpClient, resourcePath);
    }

    getAllAvailaleStock(): Observable<any> {
        return this.restHttpClient.get(this.url + '/available-stock-items');
    }

    getAllStockReceives(): Observable<any> {
        //    console.log("pano!!") ;
        return this.restHttpClient.get(this.url);
    }


    setProductCurrentStock(stockItemId: number, quantity: number): Observable<any> {
        // const resource = { stockItemId: stockItemId, quantity: quantity };
        // console.log('now setting current stock for resource : ');
        // console.log(resource);

        const params = new HttpParams()
            .append('stock-item-id', stockItemId.toString())
            .append('quantity', quantity.toString())
            ;

        return this.restHttpClient.get(this.url + '/set-product-current-stock', {
            params: params,
        });

        // return this.restHttpClient.post(this.url, JSON.stringify(resource), {
        //     headers: this.headers,
        //     params: params,
        // });
    }
}
