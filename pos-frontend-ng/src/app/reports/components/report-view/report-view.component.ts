import { Component, OnInit, AfterViewInit, ViewChild } from '@angular/core';
import { ActivatedRoute, Params } from '@angular/router';
import { FieldConfig } from '../../../dynamic-form/models/field-config.interface';
import { Validators } from '@angular/forms';
import { DynamicFormComponent } from '../../../dynamic-form/containers/dynamic-form/dynamic-form.component';
import { HttpParams } from '@angular/common/http';
import { MatTableDataSource, MatSnackBar } from '@angular/material';
import { HostListener } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { saveFile } from 'app/shared/helpers';
import { Store, select } from '@ngrx/store';
import * as fromRoot from 'app/app.reducer';
import * as sharedActions from 'shared/actions';
import * as moment from 'moment/moment';
import { ReportConfig } from 'shared/models/reports/report-config';
import { ReportsDataService } from 'shared/services/reports-data.service';
import { ReportInformation } from 'shared/models/reports/report-information';
import { ReportConfigParameter } from 'shared/models/reports/report-config-parameter';

@Component({
    selector: 'app-report-view',
    templateUrl: './report-view.component.html',
    styleUrls: ['./report-view.component.scss']
})
export class ReportViewComponent implements OnInit, AfterViewInit {
    @ViewChild(DynamicFormComponent) dynamicForm: DynamicFormComponent;

    msg: any;

    reportConfig: ReportConfig = { name: '', columns: '', nativeQuery: '', reportConfigParameters: [] };

    isLoading$: Observable<boolean>;
    IsDownloading$: Observable<boolean>;

    // uiServiceSubscription: Subscription;

    displayedColumns: string[] = [];
    dataSource = new MatTableDataSource<any>();

    dyanmicConfigParameters: FieldConfig[] = [];

    constructor(private route: ActivatedRoute,
        private reportsDataService: ReportsDataService,
        private snackbar: MatSnackBar,
        private store: Store<fromRoot.State>) {

        this.isLoading$ = this.store.pipe(select(fromRoot.getIsLoading));
        this.IsDownloading$ = this.store.pipe(select(fromRoot.getIsDownloading));
        // this.dynamicForm.setDisabled()
    }

    ngOnInit() {
        this.route.params.subscribe((res: Params) => {
            this.store.dispatch(new sharedActions.IsLoading());

            if (res.reportName && res.reportName.trim() !== '') {
                this.getReportConfigByReportName(res.reportName);
            } else {
                this.getReportConfigByReportId(res.reportId);
            }
        });
    }

    getReportConfigByReportName(reportName: string): void {
        this.reportsDataService.getReportConfigByReportName(reportName)
            .subscribe((reportConfig: ReportConfig) => {
                this.loadReportDetails(reportConfig);
            },
                (error: Error) => this.handleError(error)
            );
    }

    getReportConfigByReportId(reportId: number): void {
        this.reportsDataService.getReportConfig(reportId)
            .subscribe((reportConfig: ReportConfig) => {
                this.loadReportDetails(reportConfig);
            },
                (error: Error) => this.handleError(error)
            );
    }

    loadReportDetails(reportConfig: ReportConfig): void {
        this.reportConfig = reportConfig;
        let fields: FieldConfig[] = [];

        for (const param of reportConfig.reportConfigParameters) {
            const field = this.generateParameterFormConfig(param);
            fields.push(field);
        }

        const buttonsDisabled = this.detemineButtonsDisabledState(reportConfig.reportConfigParameters);
        const buttons = this.generateFormButtons(reportConfig, buttonsDisabled);
        fields = fields.concat(buttons);

        this.dyanmicConfigParameters = fields;
        this.store.dispatch(new sharedActions.FinishedLoading());
    }

    ngAfterViewInit() {
        let previousValid = this.dynamicForm ? this.dynamicForm.valid : null;
        this.dynamicForm.changes.subscribe(() => {
            if (this.dynamicForm.valid !== previousValid) {
                previousValid = this.dynamicForm.valid;
                this.dynamicForm.setDisabled('preview', !previousValid);
                this.dynamicForm.setDisabled('download', !previousValid);
            }
        });
    }

    handleError(error: Error) {
        this.store.dispatch(new sharedActions.FinishedLoading());
        this.store.dispatch(new sharedActions.FinishedDownloading());

        this.snackbar.open(error.message, null, {
            duration: 5000
        });
    }

    onFormSubmit(form: any) {
        // console.log(form);
        this.store.dispatch(new sharedActions.IsDownloading());

        const params = this.buildRestGetParameters(this.reportConfig.reportConfigParameters);

        this.displayedColumns = [];
        this.dataSource.data = [];
        this.reportsDataService.getReportPreview(this.reportConfig.name, params)
            .subscribe((res: ReportInformation) => {
                this.store.dispatch(new sharedActions.FinishedDownloading());
                this.dataSource.data = res.reportData;
                res.columnMetadata.forEach(col => {
                    this.displayedColumns.push(col.name);
                });
            },
                (error: Error) => this.handleError(error)
            );
    }

    downloadReport() {
        this.store.dispatch(new sharedActions.IsDownloading());
        const reportName = this.reportConfig.name;
        const params = this.buildRestGetParameters(this.reportConfig.reportConfigParameters);

        this.reportsDataService.downloadExcelReport(reportName, params)
            .subscribe((response: HttpResponse<any>) => {
                const fileName = reportName + '.xlsx';
                saveFile(response.body, fileName);
                this.store.dispatch(new sharedActions.FinishedDownloading());
            },
                (error: Error) => this.handleError(error)
            );
    }

    @HostListener('click', ['$event']) onClick(event: any) {
        const clickedButtonName = event.target.name;
        if (clickedButtonName === 'download') {
            event.stopPropagation();
            event.preventDefault();
            this.downloadReport();
        }
    }

    buildRestGetParameters(reportConfigParameters: ReportConfigParameter[]): HttpParams {
        let params = new HttpParams();

        reportConfigParameters.forEach(param => {
            let htp = this.dynamicForm.value[param.parameter];
            switch (param.parameterType) {
                case 'STRING_LIKE':
                    if (!htp || htp === 'undefined') { htp = ''; }
                    break;
                case 'SINGLE_DATE':
                    htp = moment(htp).format('YYYY-MM-DD');
                    break;
                case 'SINGLE_DATE_TIME':
                    htp = moment(htp).format('YYYY-MM-DD HH:mm:ss');
                    break;
                default:
                    htp = htp;
            }
            // console.log(htp);
            params = params.append(param.parameter, htp);
        });
        return params;
    }

    generateParameterFormConfig(param: ReportConfigParameter): FieldConfig {
        const fieldConfig: FieldConfig = {
            type: 'input',
            label: param.name,
            name: param.parameter,
            placeholder: 'Enter ' + param.name,
            validation: [Validators.required]
        };

        if (param.parameterType === 'STRING_LIKE') {
            fieldConfig.validation = null;
        } else if (param.parameterType === 'SINGLE_DATE') {
            fieldConfig.type = 'dateinput';
            fieldConfig.placeholder = 'Select ' + param.name;
            fieldConfig.value = new Date();
        } else if (param.parameterType === 'SINGLE_DATE_TIME') {
            fieldConfig.type = 'datetimeinput';
            fieldConfig.placeholder = 'Select ' + param.name;
            const date: Date = new Date();
            if (param.name.toLowerCase().includes('from') || param.name.toLowerCase().includes('start')) {
                date.setHours(0, 0, 0, 0);
            } else if (param.name.toLowerCase().includes('end') || param.name.toLowerCase().includes('to')) {
                date.setHours(23, 59, 59, 999);
            } else {
                date.setHours(0, 0, 0, 0);
            }
            fieldConfig.value = date;
        } else if (param.parameterType === 'SELECT_STATEMENT') {
            fieldConfig.type = 'select';
            fieldConfig.placeholder = 'Select ' + param.name;
            fieldConfig.parameterType = param.parameterType;
            fieldConfig.options = param.dropDownSelectOptions;
            // if (param.dropDownSelectOptions && param.dropDownSelectOptions.includes('All')) fieldConfig.value = 'All';
        } else {
            fieldConfig.validation = null;
        }

        return fieldConfig;
    }

    detemineButtonsDisabledState(params: ReportConfigParameter[]): boolean {
        let disabled = false;
        params.forEach((param) => {
            switch (param.parameterType) {
                case 'SELECT_STATEMENT':
                    disabled = true;
                    break;
                default:
            }
        });
        //        console.log('disabled : ', disabled);
        return disabled;
    }

    generateFormButtons(reportConfig: ReportConfig, buttonsDisabled: boolean): FieldConfig[] {
        const previewButtonField: FieldConfig = {
            label: 'Preview ' + reportConfig.name,
            name: 'preview',
            type: 'button',
            disabled: buttonsDisabled,
        };
        const downloadButtonField: FieldConfig = {
            label: 'Download ' + reportConfig.name,
            name: 'download',
            type: 'button',
            disabled: buttonsDisabled,
        };
        return [previewButtonField, downloadButtonField];
    }

}


