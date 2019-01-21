import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SalesSummaryReceiptPrintComponent } from './sales-summary-receipt-print.component';

describe('SalesSummaryReceiptPrintComponent', () => {
  let component: SalesSummaryReceiptPrintComponent;
  let fixture: ComponentFixture<SalesSummaryReceiptPrintComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SalesSummaryReceiptPrintComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SalesSummaryReceiptPrintComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
