import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NegativeStockSalesComponent } from './negative-stock-sales.component';

describe('NegativeStockSalesComponent', () => {
  let component: NegativeStockSalesComponent;
  let fixture: ComponentFixture<NegativeStockSalesComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ NegativeStockSalesComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NegativeStockSalesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
