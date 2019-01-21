import { TestBed } from '@angular/core/testing';

import { ScheduleOfPriceDataService } from './schedule-of-price-data.service';

describe('ScheduleOfPriceDataService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: ScheduleOfPriceDataService = TestBed.get(ScheduleOfPriceDataService);
    expect(service).toBeTruthy();
  });
});
