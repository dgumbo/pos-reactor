import {Component, OnInit} from '@angular/core';
import {AuthService} from '../../services'; 

@Component({
    selector: 'app-logout',
    templateUrl: './logout.component.html',
    styleUrls: ['./logout.component.scss']
})
export class LogoutComponent implements OnInit {

    constructor(private authService: AuthService, ) {
    }

    ngOnInit() {}

    onLogout() {
        console.log("logging out");
        this.authService.logout();
    }
}
