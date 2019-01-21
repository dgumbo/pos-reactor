import {Component} from '@angular/core';
import {Field, FieldConfig} from '../../models';
import {FormGroup} from '@angular/forms'; 
import {DateTimeAdapter} from 'ng-pick-datetime';

@Component({
    selector: 'app-form-date-input',
    templateUrl: './form-date-input.component.html', 
})
export class FormDateInputComponent implements Field {
    date: Date;
    config: FieldConfig;
    group: FormGroup; 
    
    constructor(dateTimeAdapter: DateTimeAdapter<any>) {
        dateTimeAdapter.setLocale('en');  
    }
} 
