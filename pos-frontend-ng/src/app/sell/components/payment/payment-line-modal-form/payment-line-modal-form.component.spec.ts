import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PaymentLineModalFormComponent } from './payment-line-modal-form.component';

describe('PaymentLineModalFormComponent', () => {
  let component: PaymentLineModalFormComponent;
  let fixture: ComponentFixture<PaymentLineModalFormComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PaymentLineModalFormComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PaymentLineModalFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
