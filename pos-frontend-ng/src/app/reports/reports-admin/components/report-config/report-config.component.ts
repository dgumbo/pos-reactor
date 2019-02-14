import { Component, OnInit, OnDestroy } from '@angular/core';
import { Form } from '@angular/forms';
import { ReportConfig } from 'shared/models/reports/report-config';
import { ActivatedRoute } from '@angular/router';
import { PreviousRouteService } from 'app/shared/services/previous-route.service';
import { ReportsDataService } from 'app/shared/services/reports-data.service';
import { ReportConfigParameter } from 'shared/models/reports/report-config-parameter';

declare let $: any;
@Component({
    selector: 'app-report-config',
    templateUrl: './report-config.component.html',
    styleUrls: ['./report-config.component.scss']
})
export class ReportConfigComponent implements OnInit, OnDestroy {

    isNewForm: boolean;
    reportConfig: ReportConfig;
    reportParameterTypes: string[];
    id: any;

    modalConfigParameter: ReportConfigParameter = {
        id: 0, name: '', parameter: '', columnName: '', parameterType: '',
        reportConfig: { name: '', columns: '', nativeQuery: '', },
    };
    autopopulatecolums = true;

    constructor(private route: ActivatedRoute,
        private previousRouteService: PreviousRouteService,
        private reportsDataService: ReportsDataService) {

        // this.getDropDownListItems();
        this.id = this.route.snapshot.paramMap.get('reportId');
        this.reportConfig = {
            name: '',
            columns: '',
            nativeQuery: '',
            reportConfigParameters: [],
        };

        this.getAllReportParameterTypes();

        if (this.id) {
            if (this.id === 0) {
                this.reportConfig = {
                    name: '', columns: '', nativeQuery: '',
                    reportConfigParameters: []
                };
                this.isNewForm = true;
            } else {
                this.getReportConfig(this.id);
                this.getReportConfigParameters(this.id);
            }
        }
    }

    ngOnInit() {
        $('#myModal').appendTo('body');
    }

    ngOnDestroy(): void {
        // $('body').childen('#myModal').remove();
        $('#myModal').remove();
    }

    onCancel() {
        this.previousRouteService.navigatePreviousUrl();
    }

    getReportConfig(reportConfigId: number) {
        this.reportsDataService.getReportConfig(reportConfigId)
            // .take(1)
            .subscribe((repoConf: ReportConfig) => { this.reportConfig = repoConf; });
    }

    getReportConfigParameters(reportConfigId: number) {
        this.reportsDataService.getReportConfigParameters(reportConfigId)
            .subscribe((repoConfParam: ReportConfigParameter[]) => {
                this.reportConfig.reportConfigParameters = repoConfParam;
            });
    }

    getAllReportParameterTypes() {
        this.reportsDataService.getAllReportParameterTypes()
            .subscribe((params: string[]) => { this.reportParameterTypes = params; });
    }

    onSubmit(form: Form) {
        console.log(form);
        // console.log(this.reportConfig);
        this.reportConfig.reportConfigParameters.forEach(param => {
            param.reportConfig = null;
        });

        this.reportsDataService.updateReportConfig(this.reportConfig)
            .subscribe(() => {
                this.previousRouteService.navigatePreviousUrl();
            });
    }

    removeParameter(reportConfigParameter: ReportConfigParameter) {
        const index = this.reportConfig.reportConfigParameters.indexOf(reportConfigParameter);
        this.reportConfig.reportConfigParameters.splice(index, 1);
    }

    populateReportColumns() {
        const nativeQry = this.reportConfig.nativeQuery;
        console.log(nativeQry);
        this.setReportColumns(nativeQry);
    }

    setReportColumns(nativeQry: string): void {
        const autoPopulate = this.autopopulatecolums;

        if (autoPopulate) {
            const preparedQuery = this.prepareQueryString(nativeQry);
            const columnList = this.getReportColumns(preparedQuery);

            let columns = '';
            for (let i = 0; i < columnList.length; i++) {
                columns += (columns === '' ? columnList[i] : ', ' + columnList[i]);
            }

            this.reportConfig.columns = columns;
        }
    }
    setModalReportConfigParameter(reportConfigParameter: ReportConfigParameter) {
        // console.log(reportConfigParameter) ;
        this.modalConfigParameter = reportConfigParameter;
    }

    resetModalConfigParameter() {
        this.modalConfigParameter = {
            id: 0, name: '', parameter: '', columnName: '', parameterType: '',
            reportConfig: { name: '', columns: '', nativeQuery: '', },
        };
    }

    updateReportConfigParameters(form: any) {
        //        console.log(form);
        //        console.log('form.touched : ', form.touched);
        if (form.touched) {
            if (this.modalConfigParameter.id === 0) {
                this.modalConfigParameter.id = null;
                this.modalConfigParameter.activeStatus = true;
                if (!this.reportConfig.reportConfigParameters) {
                    this.reportConfig.reportConfigParameters = [];
                }

                this.reportConfig.reportConfigParameters.push(Object.assign({}, this.modalConfigParameter));
            } else {
                // let configParameter = null;
                // if (this.modalConfigParamete                r.id ===null){
                //    configParameter = this.reportConfig.reportConfigParameters.find(s => s.name == this.modalConfigPa                rameter.name);
                // }
                // else {
                //    configParameter = this.reportConfig.reportConfigParameters.find(s => s.id == this.modalConfig                Parameter.id);
                // }

                // if (conf                igParameter) {
                //     if (this.modalConfigParame                ter.parameter)
                //         configParameter.parameter = this.modalConfigParame                ter.parameter;
                //     if (this.modalConfigP                arameter.name)
                //         configParameter.name = this.modalConfigP                arameter.name;
                //     if (this.modalConfigParamet                er.columnName)
                //         configParameter.columnName = this.modalConfigParamet                er.columnName;
                //     if (this.modalConfigParameter.                parameterType)
                //         configParameter.parameterType = this.modalConfigParameter.                parameterType;
                //     if (this.modalConfigParame                ter.selectSql)
                //         configParameter.selectSql = this.modalConfigParame                ter.selectSql;
                //     if (this.modalConfigParameter.sel                ectValueField)
                //         configParameter.selectValueField = this.modalConfigParameter.sel                ectValueField;
                //     if (this.modalConfigParam                eter.optional)
                //         configParameter.optional = this.modalConfigParam                eter.optional;
                // }

            }
        }

        this.modalConfigParameter = <ReportConfigParameter>{ id: 0, reportConfig: <ReportConfig>{} };
        form.reset();
        $('#myModal').modal('hide');
    }

    prepareQueryString(QryString: string): string {
        let nativeQry = QryString;

        nativeQry = nativeQry.replace('\n', ' ');
        nativeQry = nativeQry.replace('\r', ' ');
        nativeQry = nativeQry.replace('\t', ' ');
        nativeQry = nativeQry.replace('<', '&lt;');
        nativeQry = nativeQry.replace('<', '&gt;');
        nativeQry = nativeQry.replace('  ', ' ');
        nativeQry = nativeQry.replace('  ', ' ');
        nativeQry = nativeQry.replace('  ', ' ');
        nativeQry = nativeQry.replace('  ', ' ');
        nativeQry = nativeQry.replace('  ', ' ');
        nativeQry = nativeQry.replace('  ', ' ');
        // console.log('nativeQry.length : ' + nativeQry.length);

        let selectPosition = nativeQry.toLowerCase().indexOf('select ');

        nativeQry = nativeQry.substring(selectPosition, nativeQry.length);
        selectPosition = nativeQry.toLowerCase().indexOf('select ');
        // console.log('selectPosition : ' + selectPosition);

        let fromPosition = nativeQry.toLowerCase().indexOf('inner join');
        fromPosition = fromPosition <= 0 ? nativeQry.toLowerCase().indexOf('left join') : fromPosition;
        fromPosition = fromPosition <= 0 ? nativeQry.toLowerCase().indexOf('right join') : fromPosition;
        fromPosition = fromPosition <= 0 ? nativeQry.toLowerCase().indexOf('outer join') : fromPosition;
        // fromPosition = fromPosition <= 0 ? nativeQry.toLowerCase().indexOf(' from ') : fromPosition;
        fromPosition = fromPosition <= 0 || fromPosition <= selectPosition || fromPosition > nativeQry.length
            ? nativeQry.length : fromPosition;
        // console.log('fromPosition : ' + fromPosition);

        nativeQry = nativeQry.substring(selectPosition, fromPosition);
        // console.log('nativeQry.length : ' + nativeQry.length);
        // console.log('nativeQry : ' + nativeQry);

        // console.log(nativeQry);
        let ic = 0;
        while (nativeQry.toLowerCase().indexOf('coalesce') >= 1 && ic <= 20) {
            ic++;
            fromPosition = nativeQry.toLowerCase().indexOf('coalesce');
            let openB = 0;
            let closeB = 0;
            let iOpenned = false;

            // console.log('fromPosition : ' + fromPosition);
            for (let i = fromPosition; i < nativeQry.length; i++) {
                const t = nativeQry.substring(i, i + 1);
                if (t === '(') {
                    openB++;
                    iOpenned = true;
                }
                if (t === ')') {
                    closeB++;
                }

                // console.log('t : ' + t + ', openB : ' + openB + ', closeB : ' + closeB);

                if (openB === closeB && iOpenned === true) {
                    const toReplace = nativeQry.substring(fromPosition, i + 1);
                    // console.log('toReplace : ' + toReplace) ;
                    nativeQry = nativeQry.replace(toReplace, '');

                    break;
                }
            }
        }

        ic = 0;
        while (nativeQry.toLowerCase().indexOf('convert') >= 1 && ic <= 20) {
            ic++;
            fromPosition = nativeQry.toLowerCase().indexOf('convert');
            let openB = 0;
            let closeB = 0;
            let iOpenned = false;

            // console.log('fromPosition : ' + fromPosition);
            for (let i = fromPosition; i < nativeQry.length; i++) {
                const t = nativeQry.substring(i, i + 1);
                if (t === '(') {
                    openB++;
                    iOpenned = true;
                }
                if (t === ')') {
                    closeB++;
                }

                // console.log('t : ' + t + ', openB : ' + openB + ', closeB : ' + closeB);

                if (openB === closeB && iOpenned === true) {
                    const toReplace = nativeQry.substring(fromPosition, i + 1);
                    // console.log('toReplace : ' + toReplace) ;
                    nativeQry = nativeQry.replace(toReplace, '');

                    break;
                }
            }
        }

        ic = 0;
        while (nativeQry.toLowerCase().indexOf('isnull') >= 1 && ic <= 20) {
            ic++;
            fromPosition = nativeQry.toLowerCase().indexOf('isnull');
            let openB = 0;
            let closeB = 0;
            let iOpenned = false;

            // console.log('fromPosition : ' + fromPosition);
            for (let i = fromPosition; i < nativeQry.length; i++) {
                const t = nativeQry.substring(i, i + 1);
                if (t === '(') {
                    openB++;
                    iOpenned = true;
                }
                if (t === ')') {
                    closeB++;
                }

                // console.log('t : ' + t + ', openB : ' + openB + ', closeB : ' + closeB);

                if (openB === closeB && iOpenned === true) {
                    const toReplace = nativeQry.substring(fromPosition, i + 1);
                    // console.log('toReplace : ' + toReplace) ;
                    nativeQry = nativeQry.replace(toReplace, '');

                    break;
                }
            }
        }

        ic = 0;
        while (nativeQry.toLowerCase().indexOf('datediff') >= 1 && ic <= 20) {
            ic++;
            fromPosition = nativeQry.toLowerCase().indexOf('datediff');
            let openB = 0;
            let closeB = 0;
            let iOpenned = false;

            // console.log('fromPosition : ' + fromPosition);
            for (let i = fromPosition; i < nativeQry.length; i++) {
                const t = nativeQry.substring(i, i + 1);
                if (t === '(') {
                    openB++;
                    iOpenned = true;
                }
                if (t === ')') {
                    closeB++;
                }

                // console.log('t : ' + t + ', openB : ' + openB + ', closeB : ' + closeB);

                if (openB === closeB && iOpenned === true) {
                    const toReplace = nativeQry.substring(fromPosition, i + 1);
                    // console.log('toReplace : ' + toReplace) ;
                    nativeQry = nativeQry.replace(toReplace, '');

                    break;
                }
            }
        }

        ic = 0;
        while (nativeQry.toLowerCase().indexOf('case') >= 1 && ic <= 20) {
            ic++;
            fromPosition = nativeQry.toLowerCase().indexOf('case');
            let openB = 0;
            let closeB = 0;
            let iOpenned = false;

            // console.log('fromPosition : ' + fromPosition);
            for (let i = fromPosition; i < nativeQry.length; i++) {
                const vcase = nativeQry.substring(i, i + 5);
                const end = nativeQry.substring(i, i + 4);
                if (vcase.trim().toLowerCase() === 'case') {
                    openB++;
                    iOpenned = true;
                }
                if (end.trim().toLowerCase() === 'end') {
                    closeB++;
                }

                // console.log('--> ' + vcase + ' : ' + end + ' <--, openB : ' + openB + ', closeB : ' + closeB);

                if (openB === closeB && iOpenned === true) {
                    const toReplace = nativeQry.substring(fromPosition, i + 4);
                    // console.log('toReplace : ' + toReplace) ;
                    nativeQry = nativeQry.replace(toReplace, '');

                    break;
                }
            }
        }

        // console.log('Cleaned Qry : ' + nativeQry) ;
        return nativeQry;
    }

    getReportColumns(textareaStringValue: string): string[] {
        let query = textareaStringValue
            .trim()
            .replace('"', '')
            .replace(')', ') ')
            .replace(']', '] ')
            .replace('( ', '(')
            .replace('[ ', '[')
            .replace(/\n/g, ' ')
            .replace(/[\r\n\x0B\x0C\u0085\u2028\u2029]+/g, ' ');

        /* Replace all Double Spaces  */
        let iCountBreak = 0;
        while (query.indexOf('  ') >= 1) {
            iCountBreak += 1;
            query = query.replace('  ', ' ');

            // console.log('iCountBreak :', iCountBreak);
            if (iCountBreak >= 500) {
                break;
            }
        }
        /* End Replace all Double Spaces  */

        query = query
            .replace('( Select', '(Select')
            .replace('( select', '(select');

        /* Start Remove Commented out SQL Qry Code */
        iCountBreak = 0;
        while (query.indexOf('/*') >= 1) {
            iCountBreak += 1;
            if (iCountBreak >= 50) {
                break;
            }
            // for (let  columnMetaData in columnMetaDataList) {
            const oppeningCommentPosition = query.indexOf('/*');
            let clossingCommentPosition = 0;
            let open = 0;
            let close = 0;

            for (let i = oppeningCommentPosition; i < query.length; i++) {
                const clossingComment = query.substring(i, i + 2);

                if (clossingComment.includes('/*')) {
                    open += 1;
                } else if (clossingComment.includes('*/')) {
                    close += 1;
                    clossingCommentPosition = i;
                }

                // console.log('open : ', open, ', close : ', close );
                if (open >= 1 && close === open) {
                    const part1 = query.substring(0, oppeningCommentPosition);
                    const part2 = query.substring(clossingCommentPosition + 2, query.length);
                    query = part1 + part2;
                    break;
                }
            }
        }
        // console.log('query without /* */ comments : ');
        // console.log(query);
        /* End Remove Commented out SQL Qry Code  */


        /* Start Remove Case SQL Qry Code */
        iCountBreak = 0;
        while (query.toLowerCase().indexOf('case ') >= 1) {
            iCountBreak += 1;
            if (iCountBreak >= 50) {
                break;
            }

            // for (let  columnMetaData in columnMetaDataList) {
            const oppeningCommentPosition = query.toLowerCase().indexOf('case ');
            // console.log('case index :', oppeningCommentPosition)
            let clossingCommentPosition = 0;
            let open = 0;
            let close = 0;

            for (let i = oppeningCommentPosition; i < query.length; i++) {
                const endVar = query.substring(i, i + 4);
                const openVar = query.substring(i, i + 5);

                if (openVar.toLowerCase().includes('case ')) {
                    open += 1;
                } else if (endVar.toLowerCase().includes('end ')) {
                    close += 1;
                    clossingCommentPosition = i;
                }

                // console.log('CASE - open : ', open, ', close : ', close );
                if (open >= 1 && close === open) {
                    const part1 = query.substring(0, oppeningCommentPosition);
                    const part2 = query.substring(clossingCommentPosition + 3, query.length);
                    query = part1 + part2;
                    break;
                }
            }
        }
        // console.log('query without /* */ comments : ');
        // console.log(query);
        /* End Remove Case SQL Qry Code  */


        /* Start Remove Nested Select SQL Qry Code */
        iCountBreak = 0;
        while (query.toLowerCase().indexOf('(select') >= 1) {
            iCountBreak += 1;
            if (iCountBreak >= 50) {
                break;
            }

            // for (let  columnMetaData in columnMetaDataList) {
            const oppeningCommentPosition = query.toLowerCase().indexOf('(select');
            // console.log('case index :', query.substring( oppeningCommentPosition, oppeningCommentPosition + 50 )); 
            let clossingCommentPosition = 0;
            let open = 0;
            let close = 0;

            for (let i = oppeningCommentPosition; i < query.length; i++) {
                const char = query.substring(i, i + 1);

                if (char.toLowerCase().includes('(')) {
                    open += 1;
                } else if (char.toLowerCase().includes(')')) {
                    close += 1;
                    clossingCommentPosition = i;
                }

                // console.log('CASE - open : ', open, ', close : ', close );
                if (open >= 1 && close === open) {
                    const part1 = query.substring(0, oppeningCommentPosition);
                    const part2 = query.substring(clossingCommentPosition + 2, query.length);
                    query = part1 + part2;
                    break;
                }
            }
        }
        // console.log('query without /* */ comments : ');
        // console.log(query);
        /* End Remove Case SQL Qry Code  */



        /* Start Remove convert|cast|count|coalesce|datediff|replace SQL Qry Code */
        iCountBreak = 0;
        // while (query.toLowerCase().indexOf(/(convert|cast)/) >= 1) {
        const regex = /(convert|cast|count|coalesce|datediff|replace)/;
        while (regex.test(query.toLowerCase()) === true) {
            iCountBreak += 1;
            if (iCountBreak >= 500) {
                break;
            }

            const regex_values = regex.exec(query.toLowerCase());
            const search_value = regex_values[0];
            // console.log( 'regex_value regex_value : ', search_value);

            // for (let  columnMetaData in columnMetaDataList) {
            const oppeningCommentPosition = query.toLowerCase().indexOf(search_value);
            // let oppeningCommentPosition = query.toLowerCase().indexOf('coalesce');
            //     console.log('oppeningCommentPosition :', oppeningCommentPosition);
            //     console.log('few sql code :', query.substring(oppeningCommentPosition, oppeningCommentPosition + 31));
            //     console.log('length :', query.toLowerCase().length);
            let clossingCommentPosition = 0;
            let open = 0;
            let close = 0;

            for (let i = oppeningCommentPosition; i < query.length; i++) {
                const char = query.substring(i, i + 1);

                if (char.includes('(')) {
                    open += 1;
                }
                if (char.includes(')')) {
                    close += 1;
                    clossingCommentPosition = i;
                }

                //         console.log('Convert - open : ', open, ', close : ', close, ', i :', i)
                //         console.log('char :'', char + ''');
                if (open >= 1 && close === open) {
                    // console.log('oppeningCommentPosition : ', oppeningCommentPosition, ', clossingCommentPosition : ', clossingCommentPosition);
                    const part1 = query.substring(0, oppeningCommentPosition);
                    const part2 = query.substring(clossingCommentPosition + 1, query.length);
                    query = part1 + part2;
                    // console.log(query);
                    break;
                }
            }
        }
        // console.log('query without /* */ comments : ');
        // console.log(query);
        /* End Remove DateDiff SQL Qry Code  */




        let selectPosition = query.toLowerCase().indexOf('select'); // $NON-NLS-1$
        let fromPosition = 0;

        // console.log('selectPosition :', selectPosition);
        if (selectPosition >= 0) {
            query = query.substring(selectPosition, query.length - 1);

            selectPosition = query.trim().toLowerCase().indexOf('select '); //$NON-NLS-1$
            //     fromPosition = query.trim().toLowerCase().indexOf('inner join');
            //     fromPosition = fromPosition <= 0 ? query.trim().toLowerCase().indexOf('left join') : fromPosition;
            //     fromPosition = fromPosition <= 0 ? query.trim().toLowerCase().indexOf('right join') : fromPosition;
            //     fromPosition = fromPosition <= 0 ? query.trim().toLowerCase().indexOf('outer join') : fromPosition;
            //     fromPosition = fromPosition <= 0 ? query.trim().toLowerCase().indexOf('outer join') : fromPosition;
            fromPosition = fromPosition <= 0 ? query.trim().toLowerCase().indexOf(' from ') : fromPosition;
        }

        fromPosition = fromPosition <= 0 || fromPosition > query.length - 1 ? query.length - 1 : fromPosition;
        // console.log('fromPosition:', fromPosition);
        if (selectPosition >= 0 && selectPosition < fromPosition && query.length >= 5) {
            const columns = query.substring(selectPosition + 6, fromPosition);
            // console.log('columns :', columns);

            const columnMetaDataList = columns.split(','); // $NON-NLS-1$
            const columnList = [];

            // for (let  columnMetaData in columnMetaDataList) {
            for (let i = 0; i < columnMetaDataList.length; i++) {
                const columnMetaData = columnMetaDataList[i];

                let columnName = '';
                let open = 0;
                let close = 0;
                if (columnMetaData.includes('[') && columnMetaData.includes(']')) {
                    open = columnMetaData.toLowerCase().indexOf('[');
                    close = columnMetaData.toLowerCase().indexOf(']');
                    columnName = columnMetaData.substring(open + 1, close);
                } else if ((columnMetaData.match(/`/g) || []).length === 2) {
                    open = columnMetaData.toLowerCase().indexOf('`');
                    close = columnMetaData.toLowerCase().indexOf('`', open + 1);
                    columnName = columnMetaData.substring(open + 1, close);
                } else if ((columnMetaData.match(/'/g) || []).length === 2) {
                    open = columnMetaData.toLowerCase().indexOf('"');
                    close = columnMetaData.toLowerCase().indexOf('"', open + 1);
                    columnName = columnMetaData.substring(open + 1, close);
                } else if (columnMetaData.toLowerCase().includes(' as ')) {
                    open = columnMetaData.toLowerCase().indexOf(' as ');
                    close = columnMetaData.length;
                    columnName = columnMetaData.substring(open + 3, close).trim();
                } else if ((columnMetaData.match(/'/g) || []).length >= 4) {
                    const arr = columnMetaData.trim().split('"');
                    const size = arr.length;

                    columnName = arr[size - 2];
                } else if (columnMetaData.trim().includes('+')) {
                    close = columnMetaData.length;
                    open = columnMetaData.toLowerCase().indexOf('.');
                    columnName = columnMetaData.substring(open + 1, close).trim();
                } else if (columnMetaData.trim().split(/[\s.]+/).length >= 2 && columnMetaData.trim().split(/[\s.]+/).length <= 3) {
                    const arr = columnMetaData.trim().split(/[\s.]+/);
                    const size = arr.length;

                    columnName = arr[size - 1];
                } else {
                    columnName = columnMetaData;
                    // console.log('columnName : ' + columnName.trim() );
                }

                columnList.push(columnName);
                // console.log('columnName : ' + columnName.trim() );
            }

            // console.log(columnList);
            if (columnList) {
                return columnList;
            } else {
                return [];
            }
        } else {
            // console.log('(selectPosition >= 0 && selectPosition < fromPosition && query.length >= 5), resolved to :' + (selectPosition >= 0 && selectPosition < fromPosition && query.length >= 5));
            return [];
        }
    }
}
