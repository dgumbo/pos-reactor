import { Component  } from '@angular/core';
import {Field, FieldConfig} from '../../models';
import {FormGroup} from '@angular/forms'; 

@Component({
  selector: 'form-button-button',
  templateUrl: './form-button-button.component.html', 
})
export class FormButtonButtonComponent implements Field {  
    config: FieldConfig;
    group: FormGroup; 
}