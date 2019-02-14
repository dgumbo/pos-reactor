import { Component, OnInit } from '@angular/core';

import { Product } from 'app/shared/models';
import { StockSupplier } from 'app/shared/models/supplier';
import { SupplierDataService } from 'app/masters/services/supplier-data.service';
import { MatDialog, MatDialogConfig, MatTableDataSource } from '@angular/material';
import { StockReceive } from 'app/shared/models/stock-receive';
import { StockReceiveModalFormComponent } from '../modal-form/stock-receive-modal-form.component';
import { StockReceiveItem } from 'app/shared/models/stock-receive-item';
import { WorkFlowType } from 'app/shared/models/enums/work-flow-type';
import { Router, ActivatedRoute } from '@angular/router';
import { StockReceiveDataService } from 'app/stocks/services/stock-receive-data.service';

import { Store, select } from '@ngrx/store';
import * as fromRoot from 'app/app.reducer';
import * as stocksActions from '../../../actions';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-stock-receive',
  templateUrl: './stock-receive.component.html',
  styleUrls: ['./stock-receive.component.scss']
})
export class StockReceiveComponent implements OnInit {

  newForm = true;
  products: Product[] = [];
  suppliers: StockSupplier[] = [];

  stockReceive = <StockReceive>{};

  displayedColumns: string[] = ['action', 'product', 'supplier', 'quantity', 'totalcost', 'unitcost', 'category', 'lrcr', 'wac'];
  dataSource = new MatTableDataSource<StockReceiveItem>();

  isSubmittingStockReceive$: Observable<boolean>;

  constructor(private stockReceiveService: StockReceiveDataService,
    private supplierService: SupplierDataService,
    public dialog: MatDialog,
    private router: Router,
    private activeRoute: ActivatedRoute,
    private store: Store<fromRoot.State>) {
    let stockReceiveId = 0;
    const stockReceiveFinalizeId = this.activeRoute.snapshot.params['stockReceiveFinalizeId'];
    const stockReceivePreviewId = this.activeRoute.snapshot.params['stockReceivePreviewId'];


    if (stockReceiveFinalizeId && Number(stockReceiveFinalizeId) >= 1) {
      stockReceiveId = stockReceiveFinalizeId;
      this.newForm = true;
    } else if (stockReceivePreviewId && Number(stockReceivePreviewId) >= 1) {
      stockReceiveId = stockReceivePreviewId;
      this.newForm = false;
    }

    if (stockReceiveId && Number(stockReceiveId) >= 1) {
      // console.log('receiveId :', receiveId);
      this.stockReceiveService.get(Number(stockReceiveId))
        .subscribe((res: StockReceive) => {
          // console.log(res);
          this.stockReceive = res;
          this.stockReceive.stockReceiveItems.forEach(sri => {
            sri.totalCost = sri.unitCost * sri.quantity;
          });
          this.dataSource.data = this.stockReceive.stockReceiveItems;
        });
    } else {
      this.newMethod();
      this.stockReceive.remarks = 'Received Addition Stock';
    }

    this.isSubmittingStockReceive$ = this.store.pipe(select(fromRoot.getIsSubmittingStockReceive));
  }

  ngOnInit() {
    this.getAllAvailaleStock();
    this.getSuppliers();
    // this.stockReceive.stockReceiveItems.length
  }

  private newMethod() {
    this.stockReceive.stockReceiveItems = [];
  }

  getAllAvailaleStock() {
    this.stockReceiveService.getAllAvailaleStock()
      .subscribe((res: Product[]) => this.products = res);
  }

  getSuppliers() {
    this.supplierService.getAll()
      .subscribe((res: StockSupplier[]) => this.suppliers = res);
  }

  openDialog() {
    const dialogConfig = new MatDialogConfig();
    dialogConfig.data = { products: this.products, suppliers: this.suppliers };
    dialogConfig.width = '900px';

    const dialogRef = this.dialog.open(StockReceiveModalFormComponent, dialogConfig);

    dialogRef.afterClosed().subscribe((res: StockReceiveItem) => {
      if (res && res.product && res.quantity > 0) {
        // console.log(res);
        res.batchNumber = 'BN';
        this.stockReceive.stockReceiveItems.push(res);
        this.dataSource.data = this.stockReceive.stockReceiveItems;
        this.partialSaveStockReceive();
      }
    });
  }

  partialSaveStockReceive() {
    this.store.dispatch(new stocksActions.StartStockReceiveSubmition());
    const totalLines = this.stockReceive.stockReceiveItems.length;

    if (totalLines >= 1) {
      console.log('StockReceive : B4 Save :');
      console.log(this.stockReceive);
      this.stockReceiveService.partialSaveStockReceive(this.stockReceive)
        .subscribe(
          (res: StockReceive) => {
            this.store.dispatch(new stocksActions.CompleteStockReceiveSubmition());

          this.stockReceive = res;
          // console.log('StockReceive : After Save :');
          // console.log(res);
        },
        (error: Error) => {
          this.store.dispatch(new stocksActions.CompleteStockReceiveSubmition());
        }
        );
    }
  }

  checkPendingStockReceive() {
    this.stockReceiveService.checkPendingStockReceive()
      .subscribe((res: StockReceive) => {
        // console.log(res);
        if (res.recieveStatus === 'PENDING') {
          this.router.navigate(['/stocks', 'stock-take']);
        }
      });
  }

  saveStockReceive() {
    this.store.dispatch(new stocksActions.StartStockReceiveSubmition());

    let totalCost = 0;
    let totalLines = 0;
    // let totalLineItems = 0;
    this.stockReceive.stockReceiveItems.forEach(sri => {
      totalCost += sri.totalCost;
      totalLines += 1;
      // totalLineItems += sri.quantity;
    });

    this.stockReceive.totalCost = totalCost;
    this.stockReceive.workFlowType = WorkFlowType.PURCHASEORDER;
    if (totalLines >= 1) {
      // console.log('StockReceive : B4 Save :');
      // console.log(this.stockReceive);
      this.stockReceiveService.finalizeStockReceive(this.stockReceive)
        .subscribe(
          (res: any) => {
            this.store.dispatch(new stocksActions.CompleteStockReceiveSubmition());
            // console.log('StockReceive : After Save :');
            // console.log(res);
            this.stockReceive = <StockReceive>{};
            this.dataSource.data = [];
            this.router.navigate(['/stocks', 'stock-receive']);
          },
          (error: Error) => {
            this.store.dispatch(new stocksActions.CompleteStockReceiveSubmition());
          }
        );
    }
  }

  editStockReceiveItem(stockTakeLine: StockReceiveItem) {
    console.log(stockTakeLine);
  }

  removeStockReceiveItem(stockTakeLine: StockReceiveItem) {
    const index = this.stockReceive.stockReceiveItems.findIndex(stl => stl.product.id === stockTakeLine.product.id);

    // if (index >= 0) {
    //   this.stockTake.stockTakeLines.slice(index, 1);
    //   this.dataSource.data = this.stockTake.stockTakeLines;
    // }
  }

}
