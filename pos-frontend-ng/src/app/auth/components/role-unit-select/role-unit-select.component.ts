import {Component, OnInit} from '@angular/core';
import {AuthService} from '../../services';
import {ActivatedRoute} from '@angular/router';
import {RoleUserUnit} from '../../models';
import {FormGroup, FormControl, Validators} from '@angular/forms';

@Component({
    selector: 'app-role-unit-select',
    templateUrl: './role-unit-select.component.html',
    styleUrls: ['./role-unit-select.component.scss']
})
export class RoleUnitSelectComponent implements OnInit {
    returnUrl: string;
    roleUnitForm: FormGroup;

    roleUserUnits: RoleUserUnit[];
    selectedRoleUserUnit: RoleUserUnit;

    constructor(private authService: AuthService,
        private route: ActivatedRoute, ) {
    }

    ngOnInit() {
        this.roleUnitForm = new FormGroup({
            roleunit: new FormControl('', {validators: [Validators.required]}),
        });

        // get return url from route parameters or default to '/'
        this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/';
        
        this.authService.getUserRoleUnits().subscribe((result: RoleUserUnit[]) => {
            this.roleUserUnits = result;
        });
    }

    onSubmit() {
        this.selectedRoleUserUnit = this.roleUnitForm.value.roleunit; 
//        console.log(this.roleUnitForm.value.roleunit) ;
         
        this.authService.setRoleUserUnit(this.selectedRoleUserUnit, this.returnUrl);
    }

}
