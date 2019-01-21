import { HttpHeaders, HttpClient, HttpParams } from '@angular/common/http';
import { environment } from 'environments/environment';
import { Injectable } from '@angular/core';
import { ReportConfig } from '../models/reports/report-config';
import { ReportAnalysisSheet } from '../models/reports/report-analysis-sheet';

@Injectable()
export class StaticReportsDataService {
    private headers: HttpHeaders;
    private url: string;

    constructor(private httpClient: HttpClient) {
        this.url = environment.apiUrl + '/reports';

        this.headers = new HttpHeaders();
        this.headers = this.headers.set('Content-Type', 'application/json; charset=utf-8');
    }

    public getReportParams(relativePath: string) {
        return this.httpClient
            .get(this.url + relativePath + '/getReportParams');
    }

    public getReportColumns(relativePath: string) {
        return this.httpClient
            .get(this.url + relativePath + '/getReportColumns');
    }

    public getReportPreview(relativePath: string, params: HttpParams) {
        return this.httpClient
            .get(this.url + relativePath + '/getReportPreview', { params });
    }

    public downloadExcelReport(relativePath: string, params: HttpParams) {
        return this.httpClient
            .get(this.url + relativePath + '/downloadExcelReport'
                , {
                    params: params,
                    responseType: 'blob',
                    observe: 'response'
                }
            );
    }

    public getReportInformation(reportName: string, params: HttpParams) {
        return this.httpClient.get(this.url + '/getReportInformation/' + reportName, { params });
    }

    public getReportConfig(reportConfigId: number) {
        return this.httpClient.get(this.url + '/getReportConfig/' + reportConfigId);
    }

    public getReportConfigByReportName(reportName: string) {
        return this.httpClient.get(this.url + '/getReportConfigByReportName/' + reportName);
    }

    public getReportConfigParameters(reportConfigId: number) {
        return this.httpClient.get(this.url + '/getReportConfigParameters/' + reportConfigId);
    }

    public getReportConfigParameterOptions(reportConfigParameterId: number) {
        return this.httpClient.get(this.url + '/getReportConfigParameterOptions/' + reportConfigParameterId);
    }

    public getReportAnalysisColumns(reportConfigId: number, reportSheetName: string) {
        return this.httpClient.get(this.url + '/getReportAnalysisColumns/' + reportConfigId + '/' + reportSheetName);
    }

    public getReportAnalysisColumnsForNewSheet(reportConfigId: number) {
        return this.httpClient.get(this.url + '/getReportAnalysisColumnsForNewSheet/' + reportConfigId);
    }

    public getAllReportParameterTypes() {
        return this.httpClient.get(this.url + '/getAllReportParameterTypes');
    }

    public getAllAggregateFunctionsList() {
        return this.httpClient.get(this.url + '/getAllAggregateFunctionsList');
    }

    public getAllNumberFormatList() {
        return this.httpClient.get(this.url + '/getAllNumberFormatList');
    }

    public getAllAnalysisSectorList() {
        return this.httpClient.get(this.url + '/getAllAnalysisSectorList');
    }

    public getReportAnalysisSheet(reportConfigId: number, reportSheetName: string) {
        return this.httpClient.get(this.url + '/getReportAnalysisSheet/' + reportConfigId + '/' + reportSheetName);
    }

    public getAllReportAnalysisSheets(reportConfigId: number) {
        return this.httpClient.get(this.url + '/getAllReportAnalysisSheets/' + reportConfigId);
    }

    public updateReportConfig(reportConfig: ReportConfig) {
        return this.httpClient.post(this.url + '/updateReportConfig', JSON.stringify(reportConfig), {
            headers: this.headers
        });
    }

    public updateReportAnalysisSheet(selectedSheet: ReportAnalysisSheet) {
        return this.httpClient.post(this.url + '/updateReportAnalysisSheet', JSON.stringify(selectedSheet), {
            headers: this.headers
        });
    }

}
