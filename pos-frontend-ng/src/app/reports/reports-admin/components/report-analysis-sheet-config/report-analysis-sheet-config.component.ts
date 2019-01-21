import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { MatDialog } from '@angular/material';
import { ReportAnalysisSheet } from 'shared/models/reports/report-analysis-sheet';
import { ReportAnalysisColumn } from 'shared/models/reports/report-analysis-column';
import { PreviousRouteService } from 'shared/services/previous-route.service';
import { ReportsDataService } from 'shared/services/reports-data.service';
import { ReportConfig } from 'shared/models/reports/report-config';

declare var $: any;

@Component({
    selector: 'app-report-analysis-sheet-config',
    templateUrl: './report-analysis-sheet-config.component.html',
    styleUrls: ['./report-analysis-sheet-config.component.scss']
})
export class ReportAnalysisSheetConfigComponent implements OnInit, OnDestroy {
    isNewForm: boolean;
    isSheetUpdated: boolean;
    selectedSheet: ReportAnalysisSheet;

    reportConfigId: any;
    reportSheetName: string;

    modalReportAnalysisColumn: ReportAnalysisColumn = { name: "", analysisSector: "", reportAnalysisSheet: null };
    autopopulatecolums = true;

    aggregateFunctionsList: string[];
    numberFormatList: string[];
    analysisSectorList: string[];

    droppedAnalysisSector: string;

    constructor(private route: ActivatedRoute,
        public dialog: MatDialog,
        private previousRouteService: PreviousRouteService,
        private reportsDataService: ReportsDataService) {

        this.selectedSheet = { id: null, name: '', reportConfig: null, analysisColumnList: [], activeStatus: true, sheetProtected: false };

        this.reportConfigId = this.route.snapshot.paramMap.get('reportConfigId');
        this.reportSheetName = this.route.snapshot.paramMap.get('reportSheetName');

        this.getAllAggregateFunctionsList();
        this.getAllNumberFormatList();
        this.getAllAnalysisSectorList();

        if (this.reportConfigId) {
            if (this.reportSheetName === '0') {
                this.isNewForm = true;
                this.getReportConfig(this.reportConfigId)
                    .subscribe((reportConfig: ReportConfig) => {
                        this.selectedSheet.reportConfig = reportConfig;

                        this.getReportAnalysisColumnsForNewSheet(this.reportConfigId)
                            .subscribe((reportAnalysisColumns: ReportAnalysisColumn[]) => {
                                //                                console.log('reportAnalysisColumns');
                                //                                console.log(reportAnalysisColumns);
                                this.selectedSheet.analysisColumnList = reportAnalysisColumns;
                                this.selectedSheet.analysisColumnList.forEach(col => {
                                    col.id = null;
                                });
                            });
                    });
            } else {
                this.getReportAnalysisSheet(this.reportConfigId, this.reportSheetName);
            }
        }
    }

    getReportAnalysisSheet(reportConfigId: number, reportSheetName: string) {
        //        console.log("Starting !!");
        this.reportsDataService.getReportAnalysisSheet(reportConfigId, reportSheetName)
            // .take(1)
            .subscribe((reportSheet: ReportAnalysisSheet) => {
                //                console.log(repoConf);
                this.selectedSheet = reportSheet;

                this.getReportConfig(reportConfigId)
                    .subscribe((reportConfig: ReportConfig) => {
                        this.selectedSheet.reportConfig = reportConfig;
                    });
            });
    }

    getReportAnalysisColumns(reportConfigId: number, reportSheetName: string) {
        this.reportsDataService.getReportAnalysisColumns(reportConfigId, reportSheetName)
            .subscribe((repoAnalyColumn: ReportAnalysisColumn[]) => { this.selectedSheet.analysisColumnList = repoAnalyColumn; });
    }

    getAllAggregateFunctionsList() {
        this.reportsDataService.getAllAggregateFunctionsList()
            .subscribe((res: string[]) => { this.aggregateFunctionsList = res; });
    }

    getAllNumberFormatList() {
        this.reportsDataService.getAllNumberFormatList()
            .subscribe((res: string[]) => { this.numberFormatList = res; });
    }

    getReportConfig(reportConfigId: number) {
        return this.reportsDataService.getReportConfig(reportConfigId);
    }

    getReportAnalysisColumnsForNewSheet(reportConfigId: number) {
        return this.reportsDataService.getReportAnalysisColumnsForNewSheet(reportConfigId);
    }

    getAllAnalysisSectorList() {
        this.reportsDataService.getAllAnalysisSectorList()
            .subscribe((res: string[]) => { this.analysisSectorList = res; });
    }

    onSubmit(form: any) {
        form = form;
        if (this.isSheetUpdated) {
            // console.log(form);
            console.log(this.selectedSheet);

            this.reportsDataService.updateReportAnalysisSheet(this.selectedSheet)
                .subscribe(() => {
                    this.previousRouteService.navigatePreviousUrl();
                });
        }
    }

    onCancel() {
        this.previousRouteService.navigatePreviousUrl();
    }

    ngOnInit(): void {
        $('#myModal').appendTo("body");
    }

    ngOnDestroy(): void {
        // $("body").childen('#myModal').remove();
        $('#myModal').remove();
    }

    onChangeAnalysisSheetColumn(form: any) {
        if (form.touched && this.modalReportAnalysisColumn && this.modalReportAnalysisColumn.name) {
            this.isSheetUpdated = true;
            //            console.log("ff.touched : ", form.touched);
            let selectedAnalysisColumn = this.selectedSheet.analysisColumnList.find(s => s.name == this.modalReportAnalysisColumn.name);
            if (selectedAnalysisColumn && selectedAnalysisColumn.name) {
                // Change Report Analysis Column Sector     
                //                    console.log(selectedModalAnalysisColumn) ; 
                if (this.modalReportAnalysisColumn.analysisSector)
                    selectedAnalysisColumn.analysisSector = this.modalReportAnalysisColumn.analysisSector;
                if (this.modalReportAnalysisColumn.displayName)
                    selectedAnalysisColumn.displayName = this.modalReportAnalysisColumn.displayName;
                if (this.modalReportAnalysisColumn.position)
                    selectedAnalysisColumn.position = this.modalReportAnalysisColumn.position;
                if (this.modalReportAnalysisColumn.aggregateFunction)
                    selectedAnalysisColumn.aggregateFunction = this.modalReportAnalysisColumn.aggregateFunction;
                if (this.modalReportAnalysisColumn.showSubTotal)
                    selectedAnalysisColumn.showSubTotal = this.modalReportAnalysisColumn.showSubTotal;
                if (this.modalReportAnalysisColumn.numberFormat)
                    selectedAnalysisColumn.numberFormat = this.modalReportAnalysisColumn.numberFormat;
                if (this.modalReportAnalysisColumn.filterValue)
                    selectedAnalysisColumn.filterValue = this.modalReportAnalysisColumn.filterValue;
            }

        }
        $('#myModal').modal('hide');
        this.modalReportAnalysisColumn = { name: "", analysisSector: "", reportAnalysisSheet: null };
        form.reset();
    }

    draggableItemActionOnClickRemove($event: any) {
        this.isSheetUpdated = true;
        // Remove Report Analysis Column function  
        let $clickedButton = $($event.target);
        let analysisColumn: ReportAnalysisColumn = JSON.parse($clickedButton.attr("data-value"));
        let selectedAnalysisColumn = this.selectedSheet.analysisColumnList.find(s => s.name == analysisColumn.name);
        if (selectedAnalysisColumn) selectedAnalysisColumn.analysisSector = "None";
    }

    // Image preview function, demonstrating the ui.dialog used as a modal window
    draggableItemActionOnClickEdit($event: any) {
        let $clickedButton = $($event.target);
        let analysisColumn: ReportAnalysisColumn = JSON.parse($clickedButton.attr("data-value"));
        this.modalReportAnalysisColumn = analysisColumn;
    }

    //    //var recycle_icon = "<a href='link/to/recycle/script/when/we/have/js/off' title='Recycle this image' class='ui-icon ui-icon-refresh'>Recycle image</a>";
    acceptAnalysisColumn($event: any) {
        this.isSheetUpdated = true;
        var draggedAnalysisColumn = $event.dragData;

        // Change Report Analysis Column Sector   
        let analysisColumn: ReportAnalysisColumn = draggedAnalysisColumn;
        let selectedAnalysisColumn = this.selectedSheet.analysisColumnList.find(s => s.name == analysisColumn.name);
        if (selectedAnalysisColumn) selectedAnalysisColumn.analysisSector = this.droppedAnalysisSector;
        //console.log(selectedAnalysisColumn) ;
    }

    setDroppedTarget($event: any) {
        var targetId = $event.target && $event.target.id ? $event.target.id : "";
        if (targetId === 'None' || targetId === 'Rows' || targetId === 'Columns' || targetId === 'Filters' || targetId === 'Data')
            this.droppedAnalysisSector = targetId;

        // console.log(this.droppedAnalysisSector);
    }

}

