import { Component, OnInit } from '@angular/core';
import { Product } from 'app/shared/models';
import { MatTableDataSource } from '@angular/material';
import { CurrentStockDataService } from 'app/stocks/services/current-stock-data.service';

@Component({
  selector: 'app-current-stock',
  templateUrl: './current-stock.component.html',
  styleUrls: ['./current-stock.component.scss']
})
export class CurrentStockComponent implements OnInit {

  products: Product[] = [];
  displayedColumns: string[] = ['stockItem', 'quantity', 'stockStatus', 'productCategory', 'expiryDate'];
  dataSource = new MatTableDataSource<Product>();

  constructor(private currentStockService: CurrentStockDataService, ) { }

  ngOnInit() {
    this.getAllStockReceived();
  }

  getAllStockReceived() {
    this.currentStockService.getAllAvailaleStock()
      .subscribe(
        (res: Product[]) => {
          this.products = res
            .filter(p => p.currentStock.quantity > 0)
            .sort((a, b) => a.name < b.name ? 1 : -1);
          this.dataSource.data = this.products;
        }
      );
  }

}
