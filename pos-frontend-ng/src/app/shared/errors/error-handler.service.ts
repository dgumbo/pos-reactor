import { Injectable } from '@angular/core';
import {MatSnackBar} from '@angular/material';
import {Store} from '@ngrx/store';
import * as fromRoot from 'app/app.reducer';
import * as sharedActions from '../actions';

@Injectable( )
export class ErrorHandlerService {

  constructor(private snackbar: MatSnackBar,
        private store: Store<fromRoot.State>,) { } 

    handleError(error: Error) {
        console.log("error");
        console.log(error);
        this.store.dispatch(new sharedActions.FinishedLoading()); 
        
        this.snackbar.open(error.message, null, {
            duration: 5000
        });
    }
}
