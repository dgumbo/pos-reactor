import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { StockItemNotFoundModalComponent } from './stock-item-not-found-modal.component';

describe('StockItemNotFoundModalComponent', () => {
  let component: StockItemNotFoundModalComponent;
  let fixture: ComponentFixture<StockItemNotFoundModalComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ StockItemNotFoundModalComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(StockItemNotFoundModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
