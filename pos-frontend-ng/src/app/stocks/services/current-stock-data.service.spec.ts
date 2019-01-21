import { TestBed } from '@angular/core/testing';

import { CurrentStockDataService } from './current-stock-data.service';

describe('CurrentStockDataService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: CurrentStockDataService = TestBed.get(CurrentStockDataService);
    expect(service).toBeTruthy();
  });
});
