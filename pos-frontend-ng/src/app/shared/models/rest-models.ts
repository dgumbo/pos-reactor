import {HttpHeaders, HttpResponse, HttpParams, HttpRequest} from "@angular/common/http";
import {Observable} from "rxjs/Observable";

export type IRestTransform = (response: HttpResponse<any>) => any;

export interface IRestConfig {
  baseHeaders?: HttpHeaders;
  dynamicHeaders?: () => HttpHeaders;
  baseUrl?: string;
  path?: string;
  transform?: IRestTransform;
}

export interface IRestQuery {
  [key: string]: any;
}

export interface IOption {

  headers?: HttpHeaders | {
    [header: string]: string | string[];
  };
  observe?: 'body';
  params?: HttpParams | {
    [param: string]: string | string[];
  };
  reportProgress?: boolean;
  responseType: 'json';
  withCredentials?: boolean;
}

export interface IHttp {
  delete: (url: string, options: any) => Observable<any>;
  get: (url: string, options: any) => Observable<any>;
  head: (url: string, options: any) => Observable<any>;
  patch: (url: string, options: any, body: any) => Observable<any>;
  post: (url: string, options: any, body: any) => Observable<any>;
  put: (url: string, options: any, body: any) => Observable<any>;
  request: (url: string | HttpRequest<any>) => Observable<any>;
}