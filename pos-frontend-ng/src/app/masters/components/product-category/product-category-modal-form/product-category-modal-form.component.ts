import { Component, OnInit, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material';
import { ProductCategory } from 'app/masters/models/product-category';

@Component({
  selector: 'app-product-category-modal-form',
  templateUrl: './product-category-modal-form.component.html',
  styleUrls: ['./product-category-modal-form.component.scss']
})
export class ProductCategoryModalFormComponent implements OnInit {
  newForm = true;
  currencies = [];

  constructor(public dialogRef: MatDialogRef<ProductCategoryModalFormComponent>,
    @Inject(MAT_DIALOG_DATA) public productCategory: ProductCategory) {
    this.newForm = productCategory.id ? false : true;
  }

  ngOnInit() {
  }

  returnProductCategory() {
    this.dialogRef.close(this.productCategory);
  }
}

