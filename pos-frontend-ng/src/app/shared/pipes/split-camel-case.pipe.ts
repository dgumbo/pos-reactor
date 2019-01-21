import {Pipe, PipeTransform} from '@angular/core';

@Pipe({name: 'splitCamelCase'})
export class SplitCamelCasePipe implements PipeTransform {

    transform(value: any, args?: any): any {
        args = args;

        if (typeof value !== 'string') {
            return value;
        }

        if (value.length === 0) {
            return value ;
        }

        if (value.length <= 2) {
            return value
            .replace(/^./, (match) => match.toUpperCase());
        }

        return value
            .replace(/([A-Z])/g, (match) => ` ${match}`)
            .replace(/^./, (match) => match.toUpperCase());

    }

}
