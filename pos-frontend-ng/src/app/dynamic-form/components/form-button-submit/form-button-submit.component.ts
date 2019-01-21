import {Component} from '@angular/core';
import {FormGroup} from '@angular/forms';

import {Field, FieldConfig} from '../../models';

@Component({
  selector: 'form-button-submit',
  templateUrl: './form-button-submit.component.html' 
})
export class FormButtonSubmitComponent implements Field {
    config: FieldConfig;
    group: FormGroup;
}
