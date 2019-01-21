import { Component, OnInit } from '@angular/core';
import { NotifyService } from 'auth/services/notify.service';

@Component({
  selector: 'app-notification',
  templateUrl: './notification.component.html',
  styleUrls: ['./notification.component.scss']
})
export class NotificationComponent implements OnInit {

  constructor(public notify: NotifyService) { }

  ngOnInit() {
  }

}
