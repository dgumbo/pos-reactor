import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { StockTakeItemModalFormComponent } from './stock-take-item-modal-form.component';

describe('StockTakeItemModalFormComponent', () => {
  let component: StockTakeItemModalFormComponent;
  let fixture: ComponentFixture<StockTakeItemModalFormComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ StockTakeItemModalFormComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(StockTakeItemModalFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
