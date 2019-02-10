import { Component, OnInit } from '@angular/core';
import { StockReceive } from 'app/shared/models/stock-receive';
import { MatTableDataSource } from '@angular/material';
import { StockReceiveDataService } from 'app/stocks/services/stock-receive-data.service';

@Component({
  selector: 'app-receive-list',
  templateUrl: './receive-list.component.html',
  styleUrls: ['./receive-list.component.scss']
})
export class ReceiveListComponent implements OnInit {

  pendingStockReceive: StockReceive = null;

  stockReceiveList: StockReceive[] = [];
  displayedColumns: string[] = ['action', 'date', 'status', 'receiveCountItems', 'receiveTotalCost', 'remarks', 'user'];
  dataSource = new MatTableDataSource<StockReceive>();

  constructor(private stockReceiveService: StockReceiveDataService, ) { }

  ngOnInit() {
    this.getPendingStockReceive();
    this.getAllStockReceived();
  }

  getAllStockReceived() {
    this.stockReceiveService.getAllStockReceives()
      .subscribe(
        (res: any) => {
          this.stockReceiveList = res.sort((a, b) => a.id < b.id ? 1 : -1);
          this.dataSource.data = this.stockReceiveList;
        }
      );
  }

  getPendingStockReceive() {
    this.stockReceiveService.getPendingStockReceive()
      .subscribe((res: StockReceive) => this.pendingStockReceive = res
      );
  }

}
