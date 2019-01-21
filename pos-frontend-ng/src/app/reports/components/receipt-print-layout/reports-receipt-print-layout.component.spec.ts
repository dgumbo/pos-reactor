import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ReportsReceiptPrintLayoutComponent } from './reports-receipt-print-layout.component';

describe('ReportsReceiptPrintLayoutComponent', () => {
  let component: ReportsReceiptPrintLayoutComponent;
  let fixture: ComponentFixture<ReportsReceiptPrintLayoutComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ReportsReceiptPrintLayoutComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ReportsReceiptPrintLayoutComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
