import { NgModule } from '@angular/core';
import { CommonModule, CurrencyPipe } from '@angular/common';
import { ProductCategoryDataService, ProductDataService, ClockService } from 'shared/services';
import { HttpClientModule } from '@angular/common/http';
import {
    MatIconModule,
    MatFormFieldModule,
    MatListModule, MatInputModule,
    MatMenuModule, MatCardModule,
    MatButtonModule, MatExpansionModule,
    MatSelectModule, MatCheckboxModule,
    MatDialogModule, MatTabsModule,
    MatTableModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatSidenavModule,
    MatProgressSpinnerModule,
    MatProgressBarModule,
    MatSnackBarModule,
} from '@angular/material';
import { FooterComponent, HeaderComponent, SideNavComponent } from './components';
import { SplitCamelCasePipe, SafeUrlPipe } from './pipes';
import { RouterModule } from '@angular/router';
import { NotificationComponent } from './components/notification/notification.component';
import { OwlNativeDateTimeModule, OwlDateTimeModule, OWL_DATE_TIME_FORMATS, DateTimeAdapter, OWL_DATE_TIME_LOCALE } from 'ng-pick-datetime';
import { MomentDateTimeAdapter } from 'ng-pick-datetime-moment';
export const MY_MOMENT_FORMATS = {
    parseInput: 'DD-MMMM-YYYY HH:mm:ss',
    fullPickerInput: 'DD MMMM YYYY HH:mm:ss',
    datePickerInput: 'DD MMMM YYYY',
    timePickerInput: 'HH:mm:ss',
    monthYearLabel: 'MMMM YYYY',
    dateA11yLabel: 'LL',
    monthYearA11yLabel: 'MMMM YYYY',
};

export const sharedServices = [
    ProductDataService,
    ProductCategoryDataService,
    ClockService,
];
export const sharedComponents = [
    NotificationComponent,
    HeaderComponent,
    FooterComponent,
    SideNavComponent,
];
export const sharedModules = [
    OwlDateTimeModule,
    OwlNativeDateTimeModule,
    MatIconModule,
    MatButtonModule,
    MatCardModule,
    MatMenuModule,
    MatInputModule,
    MatListModule,
    MatFormFieldModule,
    MatExpansionModule,
    MatSelectModule,
    MatCheckboxModule,
    MatDialogModule,
    MatTabsModule,
    MatTableModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatSidenavModule,
    MatProgressSpinnerModule,
    MatProgressBarModule,
    MatSnackBarModule,
];
export const sharedPipes = [
    SplitCamelCasePipe,
    SafeUrlPipe,
];

@NgModule({
    declarations: [
        ...sharedComponents,
        ...sharedPipes,
    ],
    imports: [
        CommonModule,
        RouterModule,
        HttpClientModule,
        ...sharedModules,
    ],
    exports: [
        ...sharedComponents,
        ...sharedModules,
        ...sharedPipes,
    ],
    providers: [
        ...sharedServices,
        { provide: DateTimeAdapter, useClass: MomentDateTimeAdapter, deps: [OWL_DATE_TIME_LOCALE] },
        { provide: OWL_DATE_TIME_FORMATS, useValue: MY_MOMENT_FORMATS },
    ]
})
export class SharedModule {
}
