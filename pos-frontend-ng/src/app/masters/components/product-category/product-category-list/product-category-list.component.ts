import { Component, OnInit } from '@angular/core';
import { ProductCategory } from 'app/masters/models/product-category';
import { MatTableDataSource, MatDialog, MatDialogConfig } from '@angular/material';
import { ProductCategoryModalFormComponent } from '../product-category-modal-form/product-category-modal-form.component';
import { ProductCategoryDataService } from 'shared/services';

@Component({
  selector: 'app-product-category-list',
  templateUrl: './product-category-list.component.html',
  styleUrls: ['./product-category-list.component.scss']
})
export class ProductCategoryListComponent implements OnInit {

  paymentTypes: ProductCategory[] = [];

  displayedColumns: string[] = ['action', 'name', 'activeStatus', 'modificationTime'];
  dataSource = new MatTableDataSource<ProductCategory>();

  constructor(private productCategoryService: ProductCategoryDataService,
    public dialog: MatDialog) { }

  ngOnInit() {
    this.getProductCategorys();
  }

  getProductCategorys() {
    this.productCategoryService.getAll()
      .subscribe((res: ProductCategory[]) => {
        this.paymentTypes = res;
        this.dataSource.data = this.paymentTypes;
      });
  }

  addNewProductCategory() {
    this.openDialog(<ProductCategory>{});
  }

  editProductCategory(exchangeRate: ProductCategory) {
    this.openDialog(exchangeRate);
  }

  openDialog(exchangeRate: ProductCategory) {
    const dialogConfig = new MatDialogConfig();
    dialogConfig.data = exchangeRate;
    dialogConfig.width = '900px';

    const dialogRef = this.dialog.open(ProductCategoryModalFormComponent, dialogConfig);

    dialogRef.afterClosed().subscribe((diagRes: ProductCategory) => {
      if (diagRes && diagRes.name) {
        this.productCategoryService.create(diagRes)
          .subscribe((res: ProductCategory) => {
            const index = this.paymentTypes.findIndex(ger => ger.id === res.id);
            if (index >= 0) {
              this.paymentTypes.splice(index, 1);
            }

            this.paymentTypes.push(res);
            this.dataSource.data = this.paymentTypes;
          });
      }
    });
  }

}
