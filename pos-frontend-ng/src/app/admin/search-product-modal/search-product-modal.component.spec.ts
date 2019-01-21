import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SearchProductModalComponent } from './search-product-modal.component';

describe('SearchProductModalComponent', () => {
  let component: SearchProductModalComponent;
  let fixture: ComponentFixture<SearchProductModalComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SearchProductModalComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SearchProductModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
