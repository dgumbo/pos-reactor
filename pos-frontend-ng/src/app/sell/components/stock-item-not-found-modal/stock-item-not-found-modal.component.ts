import { Component, OnInit, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';

@Component({
  selector: 'app-stock-item-not-found-modal',
  templateUrl: './stock-item-not-found-modal.component.html',
  styleUrls: ['./stock-item-not-found-modal.component.scss']
})
export class StockItemNotFoundModalComponent implements OnInit {

  constructor(public dialogRef: MatDialogRef<StockItemNotFoundModalComponent>,
    @Inject(MAT_DIALOG_DATA) public data: String,
  ) {
    // console.log('searchStr')  ;
    // console.log(data)  ;
  }

  ngOnInit() {
  }

}
