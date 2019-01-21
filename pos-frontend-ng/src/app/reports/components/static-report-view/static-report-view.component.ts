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
import { Store } from '@ngrx/store';
import { saveFile } from 'app/shared/helpers';
import * as fromRoot from 'app/app.reducer';
import * as sharedActions from '../../../shared/actions';
import { select } from '@ngrx/store';
import * as moment from 'moment/moment';
import { ReportInformation } from 'shared/models/reports/report-information';
import { StaticReportsDataService } from 'shared/services/static-reports-data.service';
import { Menu } from 'shared/models/menu';
import { MenuGroup } from 'shared/models/menu_group';

@Component({
    selector: 'app-static-report-view',
    templateUrl: './static-report-view.component.html',
    styleUrls: ['./static-report-view.component.scss']
})
export class StaticReportViewComponent implements OnInit, AfterViewInit {
    @ViewChild(DynamicFormComponent) dynamicForm: DynamicFormComponent;

    msg: any;

    reportMenu = <Menu>{};

    reportConfigParameters: any[] = [];

    isLoading$: Observable<boolean>;
    IsDownloading$: Observable<boolean>;

    // uiServiceSubscription: Subscription;

    displayedColumns: string[] = [];
    dataSource = new MatTableDataSource<any>();

    dyanmicConfigParameters: FieldConfig[] = [];

    constructor(private route: ActivatedRoute,
        private staticReportsDataService: StaticReportsDataService,
        private snackbar: MatSnackBar,
        private store: Store<fromRoot.State>) {

        this.isLoading$ = this.store.pipe(select(fromRoot.getIsLoading));
        this.IsDownloading$ = this.store.pipe(select(fromRoot.getIsDownloading));
        // this.dynamicForm.setDisabled()

        const reportName = route.snapshot.params['reportName'];
        const menuFunction = route.snapshot.params['menuFunction'];
        // console.log(menuFunction);

        this.reportMenu = { name: reportName, menuFunction: menuFunction, menuGroup: <MenuGroup>{ name: 'Reports' } };

    }

    ngOnInit() {
        this.route.params.subscribe((res: Params) => {
            this.store.dispatch(new sharedActions.IsLoading());

            if (res.reportName && res.reportName.trim() !== '') {
                this.loadReportDetails(res.reportName);
            }
        });
    }

    loadReportDetails(reportName: String): void {
        this.staticReportsDataService.getReportParams(this.reportMenu.menuFunction)
            .subscribe((res: any[]) => {
                this.reportConfigParameters = res;

                let fields: FieldConfig[] = [];
                for (const param of res) {
                    // console.log(param);
                    const field = this.generateParameterFormConfig(param);
                    fields.push(field);
                }

                // console.log(fields);

                const buttonsDisabled = this.detemineButtonsDisabledState(res);
                const buttons = this.generateFormButtons(reportName, buttonsDisabled);
                fields = fields.concat(buttons);

                this.dyanmicConfigParameters = fields;
                this.store.dispatch(new sharedActions.FinishedLoading());
            });

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
        const params = this.buildRestGetParameters(this.reportConfigParameters);

        this.displayedColumns = [];
        this.dataSource.data = [];
        this.staticReportsDataService.getReportPreview(this.reportMenu.menuFunction, params)
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
        const params = this.buildRestGetParameters(this.reportConfigParameters);

        this.staticReportsDataService.downloadExcelReport(this.reportMenu.menuFunction, params)
            .subscribe((response: HttpResponse<any>) => {
                const fileName = this.reportMenu.name + '.xlsx';
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

    buildRestGetParameters(reportConfigParameters: any[]): HttpParams {
        let params = new HttpParams();

        reportConfigParameters.forEach(param => {
            // console.log(param.type);
            // console.log(this.dynamicForm);

            // if (param.type === 'SINGLE_DATE_TIME') {
            //     console.log(param);
            // }

            let htp = this.dynamicForm.value[param.title];
            switch (param.type) {
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
            params = params.append(param.title, htp);
        });
        // console.log(params);
        return params;
    }

    generateParameterFormConfig(param: any): FieldConfig {
        const fieldConfig: FieldConfig = {
            type: 'input',
            label: param.name,
            name: param.title,
            placeholder: 'Enter ' + param.name,
            validation: [Validators.required]
        };

        if (param.type === 'STRING_LIKE') {
            fieldConfig.validation = null;
        } else if (param.type === 'SINGLE_DATE') {
            fieldConfig.type = 'dateinput';
            fieldConfig.placeholder = 'Select ' + param.name;
            fieldConfig.value = new Date();
        } else if (param.type === 'SINGLE_DATE_TIME') {
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
            // console.log(param.name) ;
        } else if (param.type === 'SELECT_STATEMENT') {
            fieldConfig.type = 'select';
            fieldConfig.placeholder = 'Select ' + param.name;
            fieldConfig.type = param.type;
            fieldConfig.options = param.dropDownSelectOptions;
            // if (param.dropDownSelectOptions && param.dropDownSelectOptions.includes('All')) fieldConfig.value = 'All';
        } else {
            fieldConfig.validation = null;
        }

        return fieldConfig;
    }

    detemineButtonsDisabledState(params: any[]): boolean {
        let disabled = false;
        params.forEach((param) => {
            switch (param.type) {
                case 'SELECT_STATEMENT':
                    disabled = true;
                    break;
                default:
            }
        });
        //        console.log('disabled : ', disabled);
        return disabled;
    }

    generateFormButtons(reportName: String, buttonsDisabled: boolean): FieldConfig[] {
        const previewButtonField: FieldConfig = {
            label: 'Preview ' + reportName,
            name: 'preview',
            type: 'button',
            disabled: buttonsDisabled,
        };
        const downloadButtonField: FieldConfig = {
            label: 'Download ' + reportName,
            name: 'download',
            type: 'button',
            disabled: buttonsDisabled,
        };
        return [previewButtonField, downloadButtonField];
    }

}


