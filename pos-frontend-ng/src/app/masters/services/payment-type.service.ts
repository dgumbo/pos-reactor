import { Injectable } from '@angular/core';
import { DataRestService } from 'shared/services/data-rest.service';
import { HttpClient } from '@angular/common/http';

@Injectable(  {
  providedIn: 'root'
})
export class PaymentTypeService extends DataRestService {

  constructor(httpClient: HttpClient) {
    const resourcePath = '/api/masters/payment-type';
    super(httpClient, resourcePath);
  }

}
