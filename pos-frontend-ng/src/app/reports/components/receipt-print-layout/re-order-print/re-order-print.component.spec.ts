import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ReOrderPrintComponent } from './re-order-print.component';

describe('ReOrderPrintComponent', () => {
  let component: ReOrderPrintComponent;
  let fixture: ComponentFixture<ReOrderPrintComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ReOrderPrintComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ReOrderPrintComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
