import { Component, OnInit, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef, MatDialog, MatDialogConfig } from '@angular/material';
import { Product, ProductCategory } from 'shared/models';
import { StockItemSearchModalFormComponent } from '../../stock-item-search-modal-form/stock-item-search-modal-form.component';
import { StockTakeLine } from 'shared/models/stocks/stock-take-line';

@Component({
  selector: 'app-stock-take-item-modal-form',
  templateUrl: './stock-take-item-modal-form.component.html',
  styleUrls: ['./stock-take-item-modal-form.component.scss']
})
export class StockTakeItemModalFormComponent implements OnInit {

  stockTakeLine = <StockTakeLine>{ stockItem: <Product>{ productCategory: <ProductCategory>{} } };

  constructor(public dialogRef: MatDialogRef<StockTakeItemModalFormComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { products: Product[] },
    public dialog: MatDialog) {
    this.openDialog();
  }

  ngOnInit() {
  }

  ChangeProduct() {
    this.stockTakeLine = <StockTakeLine>{ stockItem: <Product>{ productCategory: <ProductCategory>{} } };
    this.openDialog();
  }

  openDialog() {
    const dialogConfig = new MatDialogConfig();
    dialogConfig.data = { products: this.data.products };
    dialogConfig.height = '600px';
    dialogConfig.width = '1000px';

    const dialogRef = this.dialog.open(StockItemSearchModalFormComponent, dialogConfig);

    dialogRef.afterClosed().subscribe((res: Product) => {
     // console.log('Selected Product : ', res.name);

      if (res && res.name) {
        this.stockTakeLine.stockItem = res;
      }
    });
  }

  returnStockItem() {
    this.dialogRef.close(this.stockTakeLine);
  }

}
