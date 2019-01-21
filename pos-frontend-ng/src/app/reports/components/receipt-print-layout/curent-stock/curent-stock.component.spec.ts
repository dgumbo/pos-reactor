import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CurrentStockReceiptReportComponent } from './curent-stock.component';

describe('CurrentStockReceiptReportComponent', () => {
  let component: CurrentStockReceiptReportComponent;
  let fixture: ComponentFixture<CurrentStockReceiptReportComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CurrentStockReceiptReportComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CurrentStockReceiptReportComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
