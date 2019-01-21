import { TestBed } from '@angular/core/testing';

import { DataRestsService } from './data-rests.service';

describe('DataRestsService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: DataRestsService = TestBed.get(DataRestsService);
    expect(service).toBeTruthy();
  });
});
