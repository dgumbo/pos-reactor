import { Component, OnInit } from '@angular/core';
import { ReportConfig } from 'shared/models/reports/report-config';
import { ReportsDataService } from 'app/shared/services/reports-data.service';

@Component({
    selector: 'app-admin-home',
    templateUrl: './admin-home.component.html',
    styleUrls: ['./admin-home.component.scss']
})
export class AdminHomeComponent implements OnInit {
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
