import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable()
export class SubscriptionService {
  constructor() { }

  getAll(): any {
    return sequence;
  }


}

// This function runs when subscribe() is called
function sequenceSubscriber(observer) {
  // synchronously deliver 1, 2, and 3, then complete
  observer.next(['BOND', 'PULA', 'RAND', 'SWIPE_ECOCASH', 'USD']);
  observer.complete();

  // unsubscribe function doesn't need to do anything in this
  // because values are delivered synchronously
  return { unsubscribe() { } };
}

// Create a new Observable that will deliver the above sequence
const sequence = new Observable(sequenceSubscriber);

// execute the Observable and print the result of each notification
sequence.subscribe({
  next(num) { console.log(num); },
  complete() { console.log('Finished sequence'); }
});

// Logs:
// 1
// 2
// 3
// Finished sequence
