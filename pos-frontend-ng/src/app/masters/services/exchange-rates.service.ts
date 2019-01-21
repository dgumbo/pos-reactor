import { Injectable } from '@angular/core';
import { DataRestService } from 'shared/services/data-rest.service';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable( )
export class ExchangeRatesService extends DataRestService {

  constructor(httpClient: HttpClient) {
    const resourcePath = '/api/exchange-rates';
    super(httpClient, resourcePath);
  }


  getCurrencies(): Observable<any> {
    return super.getAllByPath('currencies') ;
  }
}
