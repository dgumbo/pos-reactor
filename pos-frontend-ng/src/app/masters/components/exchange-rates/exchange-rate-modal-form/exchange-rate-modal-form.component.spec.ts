import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ExchangeRateModalFormComponent } from './exchange-rate-modal-form.component';

describe('ExchangeRateModalFormComponent', () => {
  let component: ExchangeRateModalFormComponent;
  let fixture: ComponentFixture<ExchangeRateModalFormComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ExchangeRateModalFormComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ExchangeRateModalFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
