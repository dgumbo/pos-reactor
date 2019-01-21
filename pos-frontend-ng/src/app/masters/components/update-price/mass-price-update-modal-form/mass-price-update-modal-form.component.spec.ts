import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MassPriceUpdateModalFormComponent } from './mass-price-update-modal-form.component';

describe('MassPriceUpdateModalFormComponent', () => {
  let component: MassPriceUpdateModalFormComponent;
  let fixture: ComponentFixture<MassPriceUpdateModalFormComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MassPriceUpdateModalFormComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MassPriceUpdateModalFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
