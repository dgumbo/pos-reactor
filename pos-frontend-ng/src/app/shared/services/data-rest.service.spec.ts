import { TestBed } from '@angular/core/testing';

import { DataRestService } from './data-rest.service';

describe('DataRestService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: DataRestService = TestBed.get(DataRestService);
    expect(service).toBeTruthy();
  });
});
