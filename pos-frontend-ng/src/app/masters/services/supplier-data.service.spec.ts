import { TestBed } from '@angular/core/testing';

import { SupplierDataService } from './supplier-data.service';

describe('SupplierDataService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: SupplierDataService = TestBed.get(SupplierDataService);
    expect(service).toBeTruthy();
  });
});
