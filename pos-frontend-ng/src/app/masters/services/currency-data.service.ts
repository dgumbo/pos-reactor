import { Injectable } from '@angular/core';
import { DataRestService } from 'shared/services/data-rest.service';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class CurrencyDataService extends DataRestService {

  constructor(httpClient: HttpClient) {
    const resourcePath = '/api/masters/currency';
    super(httpClient, resourcePath);
  }
}
