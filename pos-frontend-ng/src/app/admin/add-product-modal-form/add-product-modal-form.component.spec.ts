import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AddProductModalFormComponent } from './add-product-modal-form.component';

describe('AddProductModalFormComponent', () => {
  let component: AddProductModalFormComponent;
  let fixture: ComponentFixture<AddProductModalFormComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AddProductModalFormComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AddProductModalFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
