import { ComponentFactoryResolver, ComponentRef, Directive, Input, OnChanges, OnInit, Type, ViewContainerRef, Output } from '@angular/core';
import { FormGroup } from '@angular/forms';

import { Field } from '../../models/field.interface';
import { FieldConfig } from '../../models/field-config.interface';
import {
    FormButtonSubmitComponent, FormButtonButtonComponent,
    FormInputComponent, FormSelectComponent, FormDateInputComponent, FormDateTimeInputComponent
} from '../../components';
import { EventEmitter } from '@angular/core';

const components: { [type: string]: Type<Field> } = {
    button: FormButtonSubmitComponent,
    buttonbutton: FormButtonButtonComponent,
    buttonsubmit: FormButtonSubmitComponent,
    input: FormInputComponent,
    select: FormSelectComponent,
    dateinput: FormDateInputComponent,
    datetimeinput: FormDateTimeInputComponent
};

@Directive({
    selector: '[dynamicField]'
})
export class DynamicFieldDirective implements Field, OnChanges, OnInit {
    @Input() config: FieldConfig;

    @Input() group: FormGroup;

    @Output() buttonClick: EventEmitter<any> = new EventEmitter<any>();

    component: ComponentRef<Field>;

    constructor(
        private resolver: ComponentFactoryResolver,
        private container: ViewContainerRef
    ) { }

    ngOnChanges() {
        if (this.component) {
            this.component.instance.config = this.config;
            this.component.instance.group = this.group;
        }
    }

    ngOnInit() {
        if (!components[this.config.type]) {
            const supportedTypes = Object.keys(components).join(', ');
            throw new Error(
                `Trying to use an unsupported type (${this.config.type}).
        Supported types: ${supportedTypes}`
            );
        }
        const component = this.resolver.resolveComponentFactory<Field>(components[this.config.type]);
        this.component = this.container.createComponent(component);
        this.component.instance.config = this.config;
        this.component.instance.group = this.group;
    }
}
