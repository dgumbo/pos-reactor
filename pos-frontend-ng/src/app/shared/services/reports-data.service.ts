import { HttpHeaders, HttpClient, HttpParams } from '@angular/common/http';
import { environment } from 'environments/environment';
import { Injectable } from '@angular/core';
import { ReportConfig } from '../models/reports/report-config';
import { ReportAnalysisSheet } from '../models/reports/report-analysis-sheet';

@Injectable
    ({
        providedIn: 'root'
    })
export class ReportsDataService {
    private headers: HttpHeaders;
    private url: string;

    constructor(private httpClient: HttpClient) {
        this.url = environment.apiUrl + '/reports/mis';

        this.headers = new HttpHeaders();
        this.headers = this.headers.set('Content-Type', 'application/json; charset=utf-8');
        // this.headers = this.headers.set('Authorization',
        // 'token eyJhbGciOiJIUzI1NiJ9
        // .eyJzdWIiOiJpbXBvcnQiLCJyb2xlcyI6W10sImlzcyI6Imh0dHA6Ly9obXMucHNtaS5jby56dyIsImlhdCI6MTUzNjc5ODQ5OCwiZXhwIjoxNTM2ODE2NDk4fQ
        // .kycT1YauXGQoE8KZcj786jDUpDp_I172Q5LwPvO6v4E');
        // this.httpClient.head(this.headers) ;
    }

    public getReportList() {
        return this.httpClient.get(this.url + '/getReportList', { headers: this.headers });
    }

    public getReportColumns(reportName: string, params: HttpParams) {
        return this.httpClient.get(this.url + '/getReportColumns/' + reportName, { params });
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

    public getReportPreview(reportName: string, params: HttpParams) {
        return this.httpClient
            .get(this.url + '/getReportPreview/' + reportName, { params });
    }


    public downloadExcelReport(reportName: string, params: HttpParams) {

        return this.httpClient
            .get(this.url + '/downloadExcelReport/' + reportName
                , {
                    params: params,
                    responseType: 'blob',
                    observe: 'response'
                }
            );
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
