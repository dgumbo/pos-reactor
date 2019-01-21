import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { Store } from '@ngrx/store';
import * as fromRoot from 'app/app.reducer';
import { Observable } from 'rxjs'; 
import { select } from '@ngrx/store';
import { Menu } from 'shared/models/menu';

@Component({
    selector: 'app-side-nav',
    templateUrl: './side-nav.component.html',
    styleUrls: ['./side-nav.component.scss'],
})
export class SideNavComponent implements OnInit {
    @Output() closeSidenav = new EventEmitter<void>();

    isAuthenticated: Observable<boolean>;
    userMenus: Observable<Menu[]>;

    constructor(private store: Store<fromRoot.State>) { }

    ngOnInit() {
        this.isAuthenticated = this.store.pipe(
            select(fromRoot.getIsAuthenticated),
        );
        this.userMenus = this.store.pipe(
            select(fromRoot.getUserMenus),
        );
    }

    onClose() {
        this.closeSidenav.emit();
    }
}
