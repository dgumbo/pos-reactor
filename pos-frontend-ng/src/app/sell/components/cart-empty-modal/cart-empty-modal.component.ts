import { Component, OnInit, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';

@Component({
  selector: 'app-cart-empty-modal',
  templateUrl: './cart-empty-modal.component.html',
  styleUrls: ['./cart-empty-modal.component.scss']
})
export class CartEmptyModalComponent implements OnInit {

  constructor(public dialogRef: MatDialogRef<CartEmptyModalComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
  ) { }

  ngOnInit() { }

}
