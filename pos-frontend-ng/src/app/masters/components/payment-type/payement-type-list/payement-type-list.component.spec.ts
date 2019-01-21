import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PayementTypeListComponent } from './payement-type-list.component';

describe('PayementTypeListComponent', () => {
  let component: PayementTypeListComponent;
  let fixture: ComponentFixture<PayementTypeListComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PayementTypeListComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PayementTypeListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
