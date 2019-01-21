import {Injectable} from '@angular/core';
import {DataRestService} from 'shared/services/data-rest.service';
import {HttpClient} from '@angular/common/http';

@Injectable( )
export class ProductCategoryDataService extends DataRestService {

    constructor(httpClient: HttpClient) {
        const resourcePath = '/api/masters/product-category';
        super(httpClient, resourcePath);
    }
}
