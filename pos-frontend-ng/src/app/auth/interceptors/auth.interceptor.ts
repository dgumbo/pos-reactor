import {Injectable} from "@angular/core";
import {HttpInterceptor, HttpRequest, HttpHandler, HttpEvent, HttpHeaders} from "@angular/common/http";
//import {JwtHelperService} from "../helpers";
import {Observable} from "rxjs";
import {TokenStorageService} from "../services"; 
 
@Injectable()  
 export class AuthInterceptor implements HttpInterceptor { 
    
    constructor(private storage : TokenStorageService ){}
    
    intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> { 
//        console.log("intercepting !!") ;
//        
        // add authorization header with jwt token if available 
        let token = this.storage.getToken();   
        //console.log(token);
        if (token) { 
            request = request.clone(  { 
                setHeaders: { 
                    Authorization: 'Token ' + token 
                }
            });  
        }  

        return next.handle(request);
    } 
}