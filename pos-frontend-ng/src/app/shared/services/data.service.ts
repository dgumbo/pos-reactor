import { HttpClient, HttpHeaders } from '@angular/common/http';
import { environment } from 'environments/environment';
import { PlatformLocation } from '@angular/common';


export class DataService {
    private headers: HttpHeaders;
    private url: string;

    constructor(private httpClient: HttpClient, resourcePath: string) {
        this.url = environment.apiUrl + resourcePath;
        this.httpClient = httpClient;
        this.headers = new HttpHeaders();
        this.headers = this.headers.set('Content-Type', 'application/json; charset=utf-8');
    }

    public getAll() {
        return this.httpClient.get(this.url);
    }

    public create(resource: any) {
        return this.httpClient.post(this.url, JSON.stringify(resource), {
            headers: this.headers
        });
    }

    public get(id: any) {
        return this.httpClient.get(this.url + '/' + id);
    }

    public getByProperty(CapitalizedPropertyName: string, propertyValue: any) {
        return this.httpClient.get(this.url + '/getBy' + CapitalizedPropertyName + '/' + propertyValue);
    }

    public update(id: any, resource: any) {
        return this.httpClient.put(this.url + '/' + id, JSON.stringify(resource), {
            headers: this.headers
        });
    }
}
