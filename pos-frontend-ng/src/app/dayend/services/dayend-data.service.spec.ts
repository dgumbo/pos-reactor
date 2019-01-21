import { TestBed } from '@angular/core/testing';

import { DayendDataService } from './dayend-data.service';

describe('DayendDataService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: DayendDataService = TestBed.get(DayendDataService);
    expect(service).toBeTruthy();
  });
});
