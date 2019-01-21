import { Component, OnInit } from '@angular/core';
import { StockSupplier } from 'app/shared/models/supplier';
import { MatTableDataSource, MatDialog, MatDialogConfig } from '@angular/material';
import { SupplierDataService } from 'app/masters/services/supplier-data.service';
import { SupplierModalFormComponent } from '../supplier-modal-form/supplier-modal-form.component';

@Component({
  selector: 'app-supplier-list',
  templateUrl: './supplier-list.component.html',
  styleUrls: ['./supplier-list.component.scss']
})
export class SupplierListComponent implements OnInit {

  suppliers: StockSupplier[] = [];

  displayedColumns: string[] = ['action', 'name', 'classification', 'address'];
  dataSource = new MatTableDataSource<StockSupplier>();

  constructor(private supplierService: SupplierDataService,
    public dialog: MatDialog) { }

  ngOnInit() {
    this.getSuppliers();
  }

  getSuppliers() {
    this.supplierService.getAll()
      .subscribe((res: StockSupplier[]) => {
        this.suppliers = res;
        this.dataSource.data = this.suppliers;
      });
  }

  openDialog() {
    const dialogConfig = new MatDialogConfig();
    dialogConfig.data = <StockSupplier>{};
    dialogConfig.width = '900px';

    const dialogRef = this.dialog.open(SupplierModalFormComponent, dialogConfig);

    dialogRef.afterClosed().subscribe((res: StockSupplier) => {
      const index = this.suppliers.findIndex(s => res && s.id === res.id);

      if (index >= 0) {
        this.suppliers.splice(index, 1);
      }

      this.suppliers.push(res);
      this.dataSource.data = this.suppliers;
    });
  }

}
