import { TestBed } from '@angular/core/testing';

import { ProductCategoryDataService } from './product-category-data.service';

describe('ProductCategoryDataService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: ProductCategoryDataService = TestBed.get(ProductCategoryDataService);
    expect(service).toBeTruthy();
  });
});
