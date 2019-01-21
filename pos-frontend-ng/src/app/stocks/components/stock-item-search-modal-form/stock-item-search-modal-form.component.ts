import { Component, OnInit, HostListener, Inject, ElementRef, ViewChild } from '@angular/core';
import { Product } from 'shared/models';
import { MatTabChangeEvent, MatDialogRef, MatTableDataSource, MAT_DIALOG_DATA } from '@angular/material';

@Component({
  selector: 'app-stock-item-search-modal-form',
  templateUrl: './stock-item-search-modal-form.component.html',
  styleUrls: ['./stock-item-search-modal-form.component.scss']
})
export class StockItemSearchModalFormComponent implements OnInit {

  @ViewChild('searchTxtBox') searchTxtBox: ElementRef;

  showResultsTable = false;

  dataSource = new MatTableDataSource<Product>();
  displayedColumns: string[] = ['actions', 'name', 'availableStock', 'sellingPrice', 'barcode', 'shortcode'];

  constructor(public dialogRef: MatDialogRef<StockItemSearchModalFormComponent>,
    @Inject(MAT_DIALOG_DATA) public data: {products: Product[]}) {
  }

  ngOnInit() {
  }

  SearchProduct(txtBox: any) {
    const searchStr = txtBox.value ? txtBox.value.toLowerCase().trim() : '';

    if (searchStr.length >= 1) {
      const res = this.data.products
        .filter(p =>
          (p.barcode && p.barcode.toLowerCase().trim().includes(searchStr))
          ||
          (p.name && p.name.toLowerCase().trim().includes(searchStr))
        );
      this.dataSource.data = res;
      this.showResultsTable = true;
    } else {
      this.dataSource.data = [];
      this.showResultsTable = false;
    }
  }

  onSelect(product: Product) {
    this.returnSelectedProduct(product);
  }

  @HostListener('window:keyup.enter', ['$event', 'undefined'])
  onEnter(enterEvent) {

    const searchStr = this.searchTxtBox.nativeElement.value.trim().toLowerCase();


    const products = this.data.products
      .filter(p =>
        (p.barcode && p.barcode.toLowerCase().trim().includes(searchStr))
        ||
        (p.name && p.name.toLowerCase().trim().includes(searchStr))
      );

    if (products && products.length >= 1) {
      this.returnSelectedProduct(products[0]);
    } else {
      this.searchTxtBox.nativeElement.focus();
      this.searchTxtBox.nativeElement.select();
    }
  }

  returnSelectedProduct(product: Product) {
    this.showResultsTable = false;
    this.dialogRef.close(product);
  }

  closeDialog() {
    this.dialogRef.close();
  }

  focusSearchTextboxes(tabChangeEvent: MatTabChangeEvent) {
    const index = tabChangeEvent.index;
    if (index === 0) {
      this.searchTxtBox.nativeElement.focus();
      this.searchTxtBox.nativeElement.select();
    } else if (index === 1) {
      // Some code here if there are multiple tabs.
    }
  }
}
