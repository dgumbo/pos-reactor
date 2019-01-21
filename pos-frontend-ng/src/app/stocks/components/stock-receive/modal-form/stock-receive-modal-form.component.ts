import { Component, OnInit, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA, MatDialogConfig, MatDialog } from '@angular/material';
import { Product, ProductCategory } from 'app/shared/models';
import { SearchProductModalComponent } from 'admin/search-product-modal/search-product-modal.component';
import { StockSupplier } from 'app/shared/models/supplier';
import { StockReceiveItem } from 'app/shared/models/stock-receive-item';
import { SupplierModalFormComponent } from 'app/masters/components/suppliers/supplier-modal-form/supplier-modal-form.component';

@Component({
    selector: 'app-stock-receive-modal-form',
    templateUrl: './stock-receive-modal-form.component.html',
    styleUrls: ['./stock-receive-modal-form.component.scss']
})
export class StockReceiveModalFormComponent implements OnInit {

    stockReceiveItem = <StockReceiveItem>{ product: <Product>{ productCategory: <ProductCategory>{} } };
    suppliers: StockSupplier[] = [];
    selectedSupplierId: number;


    constructor(public dialogRef: MatDialogRef<StockReceiveModalFormComponent>,
        @Inject(MAT_DIALOG_DATA) public data: { products: Product[], suppliers: StockSupplier[] },
        public dialog: MatDialog) {
        this.openDialog();
        this.suppliers = data.suppliers;
    }

    ngOnInit() {
    }

    UpdateUnitCost() {
        if (this.stockReceiveItem.totalCost && this.stockReceiveItem.quantity && this.stockReceiveItem.quantity !== 0) {
            this.stockReceiveItem.unitCost = this.stockReceiveItem.totalCost / this.stockReceiveItem.quantity;
        } else { this.stockReceiveItem.unitCost = 0; }
    }

    ChangeProduct() {
        this.stockReceiveItem = <StockReceiveItem>{ product: <Product>{ productCategory: <ProductCategory>{} } };
        this.openDialog();
    }

    openDialog() {
        const dialogConfig = new MatDialogConfig();
        dialogConfig.data = this.data.products;
        // dialogConfig.height = '600px';
        dialogConfig.width = '1000px';

        const dialogRef = this.dialog.open(SearchProductModalComponent, dialogConfig);

        dialogRef.afterClosed().subscribe((res: Product) => {
            // console.log('Selected Product : ', res.name);

            if (res && res.name) {
                this.stockReceiveItem.product = res;
            }
        });
    }

    returnStockItem() {
        this.dialogRef.close(this.stockReceiveItem);
    }

    addSupplier() {
        const dialogConfig = new MatDialogConfig();
        dialogConfig.data = <StockSupplier>{};
        dialogConfig.width = '1000px';

        const dialogRef = this.dialog.open(SupplierModalFormComponent, dialogConfig);

        dialogRef.afterClosed().subscribe((res: StockSupplier) => {
            if (res && res.name) {
                this.suppliers.push(res);
            }
        });
    }
}
