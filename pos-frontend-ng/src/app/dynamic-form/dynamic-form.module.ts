import { NgModule } from '@angular/core';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { DynamicFieldDirective } from './directives';
import { DynamicFormComponent, } from './containers/dynamic-form/dynamic-form.component';
import {
    FormButtonSubmitComponent,
    FormButtonButtonComponent,
    FormInputComponent,
    FormSelectComponent,
    FormDateInputComponent,
    FormDateTimeInputComponent
} from './components';
import { OwlDateTimeModule, OwlNativeDateTimeModule } from 'ng-pick-datetime';
import { SharedModule } from '../shared/shared.module';
import { CommonModule } from '@angular/common';


@NgModule({
    imports: [
        CommonModule,
        SharedModule,
        FormsModule,
        ReactiveFormsModule,
    ],
    declarations: [
        DynamicFieldDirective,
        DynamicFormComponent,
        FormButtonSubmitComponent,
        FormButtonButtonComponent,
        FormInputComponent,
        FormSelectComponent,
        FormDateInputComponent,
        FormDateTimeInputComponent,
        FormButtonButtonComponent,
        FormButtonSubmitComponent
    ],
    exports: [
        DynamicFormComponent
    ],
    entryComponents: [
        FormButtonSubmitComponent,
        FormButtonButtonComponent,
        FormInputComponent,
        FormSelectComponent,
        FormDateInputComponent,
        FormDateTimeInputComponent
    ],
    providers: [
        FormsModule,
    ],
})
export class DynamicFormModule { }
