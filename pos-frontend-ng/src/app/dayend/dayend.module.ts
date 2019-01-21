import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { DayendRoutingModule } from './dayend-routing.module';
import { DayendListComponent } from './components/dayend-list/dayend-list.component';
import { DayendFormComponent } from './components/dayend-form/dayend-form.component';
import { DayendViewComponent } from './components/dayend-view/dayend-view.component';
import { DayendHomeComponent } from './components/dayend-home/dayend-home.component';
import { SharedModule } from 'shared/shared.module';
import { DayendDataService } from './services/dayend-data.service';
import { DayendModalFormComponent } from './components/dayend-modal-form/dayend-modal-form.component';
import { PaymentTypeService } from 'app/masters/services/payment-type.service';
import { FormsModule } from '@angular/forms';
import { CurrencyDataService } from 'app/masters/services/currency-data.service';

@NgModule({
  declarations: [
    DayendListComponent,
    DayendFormComponent,
    DayendViewComponent,
    DayendHomeComponent,
    DayendModalFormComponent
  ],
  imports: [
    CommonModule,
    SharedModule,
    FormsModule,
    DayendRoutingModule
  ],
  providers: [
    DayendDataService,
    PaymentTypeService,
    CurrencyDataService,
  ],
  entryComponents: [
    DayendModalFormComponent

  ],

})
export class DayendModule { }
