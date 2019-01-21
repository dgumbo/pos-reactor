import { Injectable } from '@angular/core';
import { DataRestService } from 'shared/services/data-rest.service';
import { HttpClient } from '@angular/common/http';

@Injectable( )
export class DayendDataService extends DataRestService {

  constructor(httpClient: HttpClient) {
    const resourcePath = '/api/dayend';
    super(httpClient, resourcePath);
}
}
