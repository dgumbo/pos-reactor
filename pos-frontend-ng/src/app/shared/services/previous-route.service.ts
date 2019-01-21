import { Injectable } from '@angular/core';
import { Router, NavigationEnd } from '@angular/router';

@Injectable({ providedIn: 'root' })
export class PreviousRouteService {
    private previousUrl: string;
    private currentUrl: string;

    constructor(private router: Router) {
        this.currentUrl = this.router.url;
        router.events.subscribe(event => {
            if (event instanceof NavigationEnd) {
                this.previousUrl = this.currentUrl;
                this.currentUrl = event.url;
            };
        });
    }

    public getPreviousUrl() {
        return this.previousUrl;
    }

    public navigatePreviousUrl() {
        if (this.previousUrl && this.previousUrl !== this.currentUrl) {
            console.log('Navigating Previous Url : ', this.previousUrl);
            this.router.navigate([this.previousUrl]);
        } else {
            console.log('Navigating Home - No Previous Url');
            this.router.navigate(['../']);
        }
    }
}
