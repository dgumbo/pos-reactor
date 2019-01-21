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

  stockReceiveList: StockReceive[] = [];
  displayedColumns: string[] = ['action', 'date', 'status', 'receiveCountItems', 'receiveTotalCost', 'remarks', 'user'];
  dataSource = new MatTableDataSource<StockReceive>();

  constructor(private currentStockService: StockReceiveDataService, ) { }

  ngOnInit() {
    this.getAllStockReceived();
  }

  getAllStockReceived() {
    this.currentStockService.getAllStockReceives()
      .subscribe(
        (res: any) => {
          this.stockReceiveList = res.sort((a, b) => a.id < b.id ? 1 : -1);
          this.dataSource.data = this.stockReceiveList;
        }
      );
  }

}
