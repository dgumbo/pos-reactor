import { Injectable } from '@angular/core';
import { map, share } from 'rxjs/operators';
import { Observable, interval } from 'rxjs';

@Injectable()
export class ClockService {

  private clock: Observable<Date>;

  constructor() {
    this.clock = interval(1000)
      .pipe(
        map(tick => new Date()),
        share()
      );
  }

  getClock(): Observable<Date> {
    return this.clock;
  }
}
