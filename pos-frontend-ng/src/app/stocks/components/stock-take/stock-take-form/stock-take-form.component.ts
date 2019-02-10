import { Component, OnInit } from '@angular/core';
import { Product } from 'shared/models';
import { MatTableDataSource, MatDialog, MatDialogConfig } from '@angular/material';
import { StockTakeLine } from 'shared/models/stocks/stock-take-line';
import { Router, ActivatedRoute } from '@angular/router';
import { StockTake } from 'shared/models/stocks/stock-take';
import { StockTakeService } from 'app/stocks/services/stock-take.service';
import { StockTakeItemModalFormComponent } from '../stock-take-item-modal-form/stock-take-item-modal-form.component';

@Component({
  selector: 'app-stock-take-form',
  templateUrl: './stock-take-form.component.html',
  styleUrls: ['./stock-take-form.component.scss']
})
export class StockTakeFormComponent implements OnInit {

  newForm = true;
  products: Product[] = [];

  stockTake = <StockTake>{};

  displayedColumns: string[] = ['action', 'product', 'quantity', 'batchNumber', 'category'];
  dataSource = new MatTableDataSource<StockTakeLine>();

  constructor(private stockTakeService: StockTakeService,
    public dialog: MatDialog,
    private router: Router,
    private activeRoute: ActivatedRoute) {
    const stockTakeFinalizeId = this.activeRoute.snapshot.params['stockTakeFinalizeId'];
    const stockTakePreviewId = this.activeRoute.snapshot.params['stockTakePreviewId'];
    let stockTakeId = 0;

    if (stockTakeFinalizeId && Number(stockTakeFinalizeId) >= 1) {
      stockTakeId = stockTakeFinalizeId;
      this.newForm = true;
    } else if (stockTakePreviewId && Number(stockTakePreviewId) >= 1) {
      stockTakeId = stockTakePreviewId;
      this.newForm = false;
    } else {
      this.checkPendingStockTake();
    }

    if (stockTakeId && Number(stockTakeId) >= 1) {
      this.stockTakeService.get(Number(stockTakeId))
        .subscribe((res: StockTake) => {
          // console.log(res);
          this.stockTake = res;
          this.dataSource.data = this.stockTake.stockTakeLines;
          if (stockTakeFinalizeId && stockTakeFinalizeId > 0 && res.stockTakeStatus !== 'PENDING') {
            this.router.navigate(['/stocks', 'stock-take']);
          }
        });
    } else {
      this.stockTake.stockTakeLines = [];
      this.stockTake.remarks = 'Received Addition Stock';
    }
  }

  ngOnInit() {
    this.getAllAvailaleStock();
  }

  getAllAvailaleStock() {
    this.stockTakeService.getAllStockItems()
      .subscribe((res: Product[]) => this.products = res);
  }

  editStockTakeLine(stockTakeLine: StockTakeLine) {
    console.log(stockTakeLine);
  }

  removeStockTakeLine(stockTakeLine: StockTakeLine) {
    const index = this.stockTake.stockTakeLines.findIndex(stl => stl.stockItem.id === stockTakeLine.stockItem.id);

    // if (index >= 0) {
    //   this.stockTake.stockTakeLines.slice(index, 1);
    //   this.dataSource.data = this.stockTake.stockTakeLines;
    // }
  }

  openDialog() {
    const dialogConfig = new MatDialogConfig();
    dialogConfig.data = { products: this.products };
    dialogConfig.width = '900px';

    const dialogRef = this.dialog.open(StockTakeItemModalFormComponent, dialogConfig);

    dialogRef.afterClosed().subscribe((res: StockTakeLine) => {
      console.log(res);
      if (res && res.stockItem && res.quantity > 0) {
        res.batchNumber = 'BN';
        this.stockTake.stockTakeLines.push(res);
        this.dataSource.data = this.stockTake.stockTakeLines;
        this.partialSaveStockTake();
      }
    });
  }

  saveStockTake() {
    const totalLines = this.stockTake.stockTakeLines.length;

    if (totalLines >= 1) {
      console.log('StockReceive : B4 Save :');
      console.log(this.stockTake);
      this.stockTakeService.finalizeStockTake(this.stockTake)
        .subscribe((res: any) => {
          console.log('StockReceive : After Save :');
          console.log(res);
          this.stockTake = <StockTake>{};
          this.dataSource.data = [];
          this.router.navigate(['/stocks', 'stock-take']);
        });
    }
  }

  partialSaveStockTake() {
    const totalLines = this.stockTake.stockTakeLines.length;

    if (totalLines >= 1) {
      console.log('StockReceive : B4 Save :');
      console.log(this.stockTake);
      this.stockTakeService.partialSaveStockTake(this.stockTake)
        .subscribe((res: StockTake) => {
          this.stockTake = res;
          // console.log('StockReceive : After Save :');
          // console.log(res);
        });
    }
  }

  checkPendingStockTake() {
    this.stockTakeService.checkPendingStockTake()
      .subscribe((res: StockTake) => {
        // console.log(res);
        if (res.stockTakeStatus === 'PENDING') {
          this.router.navigate(['/stocks', 'stock-take']);
        }
      });
  }
}
