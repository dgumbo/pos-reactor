import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-nav',
  templateUrl: './nav.component.html',
  styleUrls: ['./nav.component.scss']
})
export class NavComponent implements OnInit {

  constructor(private router: Router/*private auth: AuthService*/) { }

  ngOnInit() { }

  logout() {
    //    this.auth.signOut();
  }

  showTestPrint() {
    console.log('gsgs');

    this.router.navigate(['sell',
      {
        outlets: {
          'print': ['testprint']
        }
      }]);
  }
}
