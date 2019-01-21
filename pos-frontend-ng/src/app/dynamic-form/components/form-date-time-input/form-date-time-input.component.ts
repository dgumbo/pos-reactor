import { Component } from '@angular/core';
import { Field, FieldConfig } from '../../models';
import { FormGroup } from '@angular/forms';

@Component({
    selector: 'app-form-date-time-input',
    templateUrl: './form-date-time-input.component.html',
})
export class FormDateTimeInputComponent implements Field {
    date: Date;
    config: FieldConfig;
    group: FormGroup;

    constructor() {
        this.date = new Date();
    }
}
