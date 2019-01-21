import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PriceUpdateModalFormComponent } from './price-update-modal-form.component';

describe('PriceUpdateModalFormComponent', () => {
  let component: PriceUpdateModalFormComponent;
  let fixture: ComponentFixture<PriceUpdateModalFormComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PriceUpdateModalFormComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PriceUpdateModalFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
