import { TestBed, inject } from '@angular/core/testing';

import { LocalDataSourceConverterService } from './local-data-source-converter.service';

describe('LocalDataSourceConverterService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [LocalDataSourceConverterService]
    });
  });

  it('should be created', inject([LocalDataSourceConverterService], (service: LocalDataSourceConverterService) => {
    expect(service).toBeTruthy();
  }));
});
