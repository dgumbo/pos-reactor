import { TestBed } from '@angular/core/testing';

import { ExchangeRatesService } from './exchange-rates.service';

describe('ExchangeRatesService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: ExchangeRatesService = TestBed.get(ExchangeRatesService);
    expect(service).toBeTruthy();
  });
});
