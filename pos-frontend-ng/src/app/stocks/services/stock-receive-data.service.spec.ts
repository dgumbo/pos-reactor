import { TestBed } from '@angular/core/testing';
import { StockReceiveDataService } from './stock-receive-data.service';

describe('StockReceiveDataService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: StockReceiveDataService = TestBed.get(StockReceiveDataService);
    expect(service).toBeTruthy();
  });
});
