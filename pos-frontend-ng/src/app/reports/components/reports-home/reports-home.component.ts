import { Component, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material';
import { Observable } from 'rxjs';
import { Store, select } from '@ngrx/store';
import * as fromRoot from 'app/app.reducer';
import * as  SharedActions from 'shared/actions';
import { Menu } from 'shared/models/menu';
import { MenuGroup } from 'shared/models/menu_group';
import { ReportsPrintService } from 'app/reports/services/reports-print.service';
import { ReportsDataService } from 'shared/services/reports-data.service';
import { ReportConfig } from 'shared/models/reports/report-config';

@Component({
    selector: 'app-reports-home',
    templateUrl: './reports-home.component.html',
    styleUrls: ['./reports-home.component.scss']
})
export class ReportsHomeComponent implements OnInit {
    msg: any;
    dynamicReportMenus: Observable<Menu[]>;
    isLoading$: Observable<boolean>;

    staticReports: Menu[] = [];
    receiptPrintReports: Menu[] = [];

    constructor(private snackbar: MatSnackBar,
        private store: Store<fromRoot.State>,
        private reportsPrintService: ReportsPrintService,
        private reportsDataService: ReportsDataService) {
        this.isLoading$ = this.store.pipe(select(fromRoot.getIsLoading));
    }

    ngOnInit() {
        this.dynamicReportMenus = this.store.pipe(
            select(fromRoot.getUserMenus),
        );

        // this.dynamicReportMenus.subscribe(res => console.log('pano : ' , res));

        this.getDynamicReports();
        this.populateStaticReports();
        this.populateReceiptPrintReports();
    }

    getDynamicReports() {
        this.reportsDataService.getReportList()
            .subscribe((res: ReportConfig[]) => {
                // console.log(res) ;
                const menus: Menu[] = [];

                res.forEach(rc => {
                    const menu = <Menu>{
                        name: rc.name,
                        menuFunction: rc.name,
                        menuGroup: { name: 'Dynamic Reports' },
                        type: 'Dynamic Reports',
                    };
                    menus.push(menu);
                });

                // console.log(menus);
                this.store.dispatch(new SharedActions.SetUserMenus(menus));
            });

    }

    populateStaticReports() {
        this.staticReports.push(
            <Menu>{ name: 'Sales Summary Report', menuFunction: '/sales/summary', menuGroup: <MenuGroup>{ name: 'Reports' } }
        );
        this.staticReports.push(
            <Menu>{ name: 'Sales Receipts Report', menuFunction: '/sales/receipts', menuGroup: <MenuGroup>{ name: 'Reports' } }
        );
        this.staticReports.push(
            <Menu>{ name: 'Current Stock Report', menuFunction: '/stocks/current', menuGroup: <MenuGroup>{ name: 'Reports' } }
        );
        this.staticReports.push(
            <Menu>{ name: 'Stock Out Report', menuFunction: '/stocks/out', menuGroup: <MenuGroup>{ name: 'Reports' } }
        );
        this.staticReports.push(
            <Menu>{ name: 'Stock ReOrder Report', menuFunction: '/stocks/re-order', menuGroup: <MenuGroup>{ name: 'Reports' } }
        );
    }

    populateReceiptPrintReports() {
        this.receiptPrintReports.push(
            <Menu>{ name: 'Print Stock ReOrder Report', menuFunction: 're-order', menuGroup: <MenuGroup>{ name: 'Print Reports' } }
        );
        this.receiptPrintReports.push(
            <Menu>{
                name: 'Print Sales Summary Report - Detailed',
                menuFunction: 'sales-summary-detailed',
                menuGroup: <MenuGroup>{ name: 'Print Reports' }
            }
        );
        this.receiptPrintReports.push(
            <Menu>{
                name: 'Print Sales Summary Report - Summarized',
                menuFunction: 'sales-summary-summarized',
                menuGroup: <MenuGroup>{ name: 'Print Reports' }
            }
        );
        this.receiptPrintReports.push(
            <Menu>{ name: 'Print Current Stock Report', menuFunction: 'current-stock', menuGroup: <MenuGroup>{ name: 'Print Reports' } }
        );
    }

    handleError(error: Error) {
        console.log('error !!');
        //     this.store.dispatch(new sharedActions.FinishedLoading());
        this.snackbar.open(error.message, null, {
            duration: 5000
        });
    }



    notifyPrinterModule() {
        // console.log('pano') ;
        this.reportsPrintService
            .printStockOutRecieptReport();
    }

}
