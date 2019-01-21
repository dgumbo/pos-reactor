import { Component, OnInit } from '@angular/core';
import { ReportsDataService } from 'app/shared/services/reports-data.service';
import { ReportConfig } from 'shared/models/reports/report-config';

@Component({
    selector: 'app-report-list',
    templateUrl: './report-list.component.html',
    styleUrls: ['./report-list.component.scss']
})
export class ReportListComponent implements OnInit {
    reportConfigList: ReportConfig[] = [];

    constructor(private reportsDataService: ReportsDataService) { }

    ngOnInit() {
        this.getReportList();
    }

    getReportList() {
        this.reportsDataService.getReportList().subscribe((prods: ReportConfig[]) => {
            this.reportConfigList = prods;
        });
    }

}
