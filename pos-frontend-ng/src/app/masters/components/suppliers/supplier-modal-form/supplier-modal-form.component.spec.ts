import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SupplierModalFormComponent } from './supplier-modal-form.component';

describe('SupplierModalFormComponent', () => {
  let component: SupplierModalFormComponent;
  let fixture: ComponentFixture<SupplierModalFormComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SupplierModalFormComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SupplierModalFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
