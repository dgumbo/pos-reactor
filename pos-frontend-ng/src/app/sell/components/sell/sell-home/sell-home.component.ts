import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-sell-home',
  templateUrl: './sell-home.component.html',
  styleUrls: ['./sell-home.component.scss']
})
export class SellHomeComponent implements OnInit {

  dateSell: boolean;

  constructor(route: ActivatedRoute) {
    this.dateSell = route.snapshot.params['date-sell'] ? true : false;
  }

  ngOnInit() {  }

}
