import {Component, OnInit} from '@angular/core';
import {FormControl, Validators, FormGroup} from '@angular/forms';
import {AuthService} from '../../services';
import {User} from '../../models';
import {ActivatedRoute} from '@angular/router';

@Component({
    selector: 'app-login',
    templateUrl: './login.component.html',
    styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
    returnUrl: string;
    loginForm: FormGroup;

    constructor(private authService: AuthService,
        private route: ActivatedRoute, ) {
    }

    ngOnInit() { 
        this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/';

//        this.loginForm = new FormGroup({
//            username: new FormControl('import', {validators: [Validators.required]}),
//            password: new FormControl('import', {validators: [Validators.required]})
//        });

        this.loginForm = new FormGroup({
            username: new FormControl('', {validators: [Validators.required]}),
            password: new FormControl('', {validators: [Validators.required]})
        });
    }

    onSubmit() {
        var user: User = {
            username: this.loginForm.value.username,
            password: this.loginForm.value.password
        };
        //    console.log(user); 
        this.authService.login(user, this. returnUrl) ;
    }    
}
