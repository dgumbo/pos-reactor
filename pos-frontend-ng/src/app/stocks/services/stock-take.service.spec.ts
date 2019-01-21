import { TestBed } from '@angular/core/testing';

import { StockTakeService } from './stock-take.service';

describe('StockTakeService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: StockTakeService = TestBed.get(StockTakeService);
    expect(service).toBeTruthy();
  });
});
