import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { StockItemSearchModalFormComponent } from './stock-item-search-modal-form.component';

describe('StockItemSearchModalFormComponent', () => {
  let component: StockItemSearchModalFormComponent;
  let fixture: ComponentFixture<StockItemSearchModalFormComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ StockItemSearchModalFormComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(StockItemSearchModalFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
