import { Injectable } from '@angular/core';
import { DataRestService } from 'shared/services/data-rest.service';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Cart } from 'shared/models';

@Injectable()
export class SellService extends DataRestService {
    constructor(httpClient: HttpClient) {
        const resourcePath = '/rest/sell';
        super(httpClient, resourcePath);
    }

    processSell(cart: Cart): Observable<any> {
        // console.log(cart);
        return this.restHttpClient.put(this.url + '/sell', JSON.stringify(cart), {
            headers: this.headers
        });
    }
}
