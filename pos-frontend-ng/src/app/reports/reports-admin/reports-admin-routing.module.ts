import {NgModule} from '@angular/core';
import {Routes, RouterModule} from '@angular/router';
import {ReportListComponent} from './components/report-list/report-list.component';
import {AdminHomeComponent} from './components/admin-home/admin-home.component';
import {ReportConfigComponent} from './components/report-config/report-config.component';
import {ReportAnalysisSheetListComponent} from './components/report-analysis-sheet-list/report-analysis-sheet-list.component';
import {ReportAnalysisSheetConfigComponent} from './components/report-analysis-sheet-config/report-analysis-sheet-config.component';

const routes: Routes = [
    {
        path: '',
        component: AdminHomeComponent
    },
    {
        path: 'report-list',
        component: ReportListComponent
    },
    {
        path: 'report-config/:reportId',
        component: ReportConfigComponent
    },
    {
        path: 'report-analysis-sheet-list/:reportId',
        component: ReportAnalysisSheetListComponent
    },
    {
        path: 'report-analysis-sheet-config/:reportConfigId/:reportSheetName',
        component: ReportAnalysisSheetConfigComponent
    },

];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ReportsAdminRoutingModule { }
