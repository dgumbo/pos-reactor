import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CartEmptyModalComponent } from './cart-empty-modal.component';

describe('CartEmptyModalComponent', () => {
  let component: CartEmptyModalComponent;
  let fixture: ComponentFixture<CartEmptyModalComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CartEmptyModalComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CartEmptyModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
