import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ReportsAdminRoutingModule } from './reports-admin-routing.module'; 
import { NgDragDropModule } from 'ng-drag-drop';
import { ReportListComponent } from './components/report-list/report-list.component';
import { ReportConfigComponent } from './components/report-config/report-config.component';
import { ReportAnalysisSheetListComponent } from './components/report-analysis-sheet-list/report-analysis-sheet-list.component';
import { ReportAnalysisSheetConfigComponent } from './components/report-analysis-sheet-config/report-analysis-sheet-config.component';
import { AdminHomeComponent } from './components/admin-home/admin-home.component';
import { AuthModule } from 'auth/auth.module';
import { SharedModule } from 'shared/shared.module';
import {FormsModule} from '@angular/forms';

@NgModule({
    imports: [
        CommonModule,
        SharedModule,
        ReportsAdminRoutingModule,
        AuthModule, 
        FormsModule,
        NgDragDropModule.forRoot()
    ],
    declarations: [
        ReportListComponent,
        ReportConfigComponent,
        ReportAnalysisSheetListComponent,
        ReportAnalysisSheetConfigComponent,
        AdminHomeComponent,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ReportsAdminModule { }
