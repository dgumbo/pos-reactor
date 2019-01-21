import { Component, OnInit } from '@angular/core';
import { Dayend } from 'shared/models/dayend/dayend';
import { MatTableDataSource } from '@angular/material';
import { DayendDataService } from 'app/dayend/services/dayend-data.service';

@Component({
  selector: 'app-dayend-list',
  templateUrl: './dayend-list.component.html',
  styleUrls: ['./dayend-list.component.scss']
})
export class DayendListComponent implements OnInit {

  dayendList: Dayend[] = [];
  displayedColumns: string[] = ['action', 'date', 'status', 'receiveCountItems', 'receiveTotalCost', 'remarks', 'user'];
  dataSource = new MatTableDataSource<Dayend>();

  constructor(private dayendService: DayendDataService, ) { }

  ngOnInit() {
    this.getAllStockReceived();
  }

  getAllStockReceived() {
    this.dayendService.getAll()
      .subscribe(
        (res: any) => {
          this.dayendList = res.sort((a, b) => a.id < b.id ? 1 : -1);
          this.dataSource.data = this.dayendList;
        }
      );
  }

  filter() {

  }

  printDayEnd(dayEnd) {

  }

}
