import { Component, OnInit } from '@angular/core';
import { ReportConfig } from 'shared/models/reports/report-config';
import { ActivatedRoute } from '@angular/router';
import { ReportsDataService } from 'app/shared/services/reports-data.service';
import { ReportAnalysisSheet } from 'shared/models/reports/report-analysis-sheet';
import { MatTableDataSource } from '@angular/material';

@Component({
    selector: 'app-report-analysis-sheet-list',
    templateUrl: './report-analysis-sheet-list.component.html',
    styleUrls: ['./report-analysis-sheet-list.component.scss']
})
export class ReportAnalysisSheetListComponent implements OnInit {

    // reportAnalysisSheets: string[];
    isNewForm: boolean;
    id: any;
    reportConfig: ReportConfig;
    displayedColumns: string[] = ['actions', 'name', 'filters', 'rows', 'columns', 'data'];
    dataSource = new MatTableDataSource<ReportAnalysisSheet>();

    constructor(private route: ActivatedRoute,
        private reportsDataService: ReportsDataService) {

        // this.getDropDownListItems();
        this.id = this.route.snapshot.paramMap.get('reportId');
        this.reportConfig = {
            name: '',
            columns: '',
            nativeQuery: '',
            reportConfigParameters: [],
            reportAnalysisSheets: []
        };

        // this.getAllReportParameterTypes();

        if (this.id) {
            if (this.id === 0) {
                this.reportConfig = {
                    name: '', columns: '', nativeQuery: '',
                    reportConfigParameters: [],
                    reportAnalysisSheets: []
                };
                this.isNewForm = true;
            } else {
                this.getReportConfig(this.id)
                    .subscribe((repoConf: ReportConfig) => {
                        this.reportConfig = repoConf;

                        this.getAllReportAnalysisSheets(this.id)
                            .subscribe((repoConfSheets: ReportAnalysisSheet[]) => {
                                this.reportConfig.reportAnalysisSheets = repoConfSheets;
                                this.dataSource.data = this.reportConfig.reportAnalysisSheets;
                            });
                    });

                this.getAllReportAnalysisSheets(this.id);
            }
        }
    }

    ngOnInit(): void {
    }

    getReportConfig(reportConfigId: number) {
        return this.reportsDataService.getReportConfig(reportConfigId);
    }

    getAllReportAnalysisSheets(reportConfigId: number) {
        return this.reportsDataService.getAllReportAnalysisSheets(reportConfigId);
    }

    removeAnalysisSheet(reportAnalysisSheet: ReportAnalysisSheet) {
        const index = this.reportConfig.reportAnalysisSheets.indexOf(reportAnalysisSheet);
        // console.log('index:', index);
        this.reportConfig.reportAnalysisSheets.splice(index, 1);
        this.dataSource.data = this.reportConfig.reportAnalysisSheets;
    }

}
