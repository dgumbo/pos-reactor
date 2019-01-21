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

  constructor(private currentStockService: StockReceiveDataService,
    private supplierService: SupplierDataService,
    public dialog: MatDialog,
    private router: Router,
    private activeRoute: ActivatedRoute) {
    const receiveId = this.activeRoute.snapshot.params['receiveId'];

    if (receiveId && Number(receiveId) >= 1) {
      // console.log('receiveId :', receiveId);
      this.currentStockService.get(Number(receiveId))
        .subscribe((res: StockReceive) => {
          // console.log(res);
          this.newForm = false;
          this.stockReceive = res;
          this.stockReceive.stockReceiveItems.forEach(sri => {
            sri.totalCost = sri.unitCost * sri.quantity;
          });
          this.dataSource.data = this.stockReceive.stockReceiveItems;
        });
    } else {
      this.stockReceive.stockReceiveItems = [];
      this.stockReceive.remarks = 'Received Addition Stock';
    }

  }

  ngOnInit() {
    this.getAllAvailaleStock();
    this.getSuppliers();
    // this.stockReceive.stockReceiveItems.length
  }

  getAllAvailaleStock() {
    this.currentStockService.getAllAvailaleStock()
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
      }
    });
  }

  saveStockReceive() {
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
      this.currentStockService.createStockReceive(this.stockReceive)
        .subscribe((res: any) => {
          // console.log('StockReceive : After Save :');
          // console.log(res);
          this.stockReceive = <StockReceive>{};
          this.dataSource.data = [];
          this.router.navigate(['/stocks', 'stock-receive']);
        });
    }
  }

}
