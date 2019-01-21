import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { DataService } from '../data.service';

@Injectable()
export class ServiceCategoryService extends DataService {
    constructor(httpClient: HttpClient) {
        const resourcePath = 'rest/servicecategories';
        super(httpClient, resourcePath);
    }
}
