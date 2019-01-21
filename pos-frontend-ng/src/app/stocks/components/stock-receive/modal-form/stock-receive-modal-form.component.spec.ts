import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { StockReceiveModalFormComponent } from './stock-receive-modal-form.component';

describe('StockReceiveModalFormComponent', () => {
  let component: StockReceiveModalFormComponent;
  let fixture: ComponentFixture<StockReceiveModalFormComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ StockReceiveModalFormComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(StockReceiveModalFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
