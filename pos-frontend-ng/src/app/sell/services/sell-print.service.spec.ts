import { TestBed } from '@angular/core/testing';

import { SellPrintService } from './sell-print.service';

describe('SellPrintService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: SellPrintService = TestBed.get(SellPrintService);
    expect(service).toBeTruthy();
  });
});
