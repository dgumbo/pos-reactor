import { ValidatorFn } from '@angular/forms';

export interface FieldConfig {
  name: string;
  label?: string;
  type: string;
  parameterType?: string;
  disabled?: boolean;
  options?: string[];
  placeholder?: string;
  validation?: ValidatorFn[];
  value?: any;
}
