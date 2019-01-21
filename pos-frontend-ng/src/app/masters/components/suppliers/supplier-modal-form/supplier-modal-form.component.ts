import { Component, OnInit, Inject } from '@angular/core';
import { StockSupplier } from 'app/shared/models/supplier';
import { MatDialogRef } from '@angular/material';
import { SupplierDataService } from 'app/masters/services/supplier-data.service';

@Component({
  selector: 'app-supplier-modal-form',
  templateUrl: './supplier-modal-form.component.html',
  styleUrls: ['./supplier-modal-form.component.scss']
})
export class SupplierModalFormComponent implements OnInit {

  stockSupplier = <StockSupplier>{};

  constructor(public dialogRef: MatDialogRef<SupplierModalFormComponent>,
    private supplierService: SupplierDataService) {
  }

  ngOnInit() {
  }

  returnStockSupplier() {
    if (this.stockSupplier && this.stockSupplier.name) {
      this.supplierService.create(this.stockSupplier)
        .subscribe(sup => {
          this.dialogRef.close(sup);
        });
    }

  }
}

