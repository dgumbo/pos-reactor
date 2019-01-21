import { Injectable } from '@angular/core';
import {DataRestService} from 'shared/services/data-rest.service';
import {HttpClient} from '@angular/common/http';

@Injectable( )
export class ProductDataService extends DataRestService {

    constructor(httpClient: HttpClient) {
        const resourcePath =  '/rest/products';
        super(httpClient, resourcePath);
    }
}
