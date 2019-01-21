import { Component, OnInit } from '@angular/core';
import { MatTableDataSource } from '@angular/material';
import { StockTake } from 'shared/models/stocks/stock-take';
import { StockTakeService } from 'app/stocks/services/stock-take.service';

@Component({
  selector: 'app-stock-take-list',
  templateUrl: './stock-take-list.component.html',
  styleUrls: ['./stock-take-list.component.scss']
})
export class StockTakeListComponent implements OnInit {

  stockReceiveList: StockTake[] = [];
  displayedColumns: string[] = ['action', 'date', 'status', 'receiveCountItems', 'remarks', 'user'];
  dataSource = new MatTableDataSource<StockTake>();

  pendingStockTake: StockTake = null;

  constructor(private stockTakeService: StockTakeService, ) { }

  ngOnInit() {
    this.getPendingStockTake();
    this.getAllStockTakes();
  }

  getPendingStockTake() {
    this.stockTakeService.getPendingStockTake()
      .subscribe((res: StockTake) => this.pendingStockTake = res
      );
  }

  getAllStockTakes() {
    this.stockTakeService.getAll()
      .subscribe(
        (res: StockTake[]) => {
          this.stockReceiveList = res.sort((a, b) => a.id < b.id ? 1 : -1);
          this.dataSource.data = this.stockReceiveList;
        }
      );
  }

}
