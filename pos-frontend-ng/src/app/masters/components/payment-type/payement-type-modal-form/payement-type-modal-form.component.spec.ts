import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PayementTypeModalFormComponent } from './payement-type-modal-form.component';

describe('PayementTypeModalFormComponent', () => {
  let component: PayementTypeModalFormComponent;
  let fixture: ComponentFixture<PayementTypeModalFormComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PayementTypeModalFormComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PayementTypeModalFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
