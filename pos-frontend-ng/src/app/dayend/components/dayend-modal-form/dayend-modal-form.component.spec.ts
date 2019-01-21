import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DayendModalFormComponent } from './dayend-modal-form.component';

describe('DayendModalFormComponent', () => {
  let component: DayendModalFormComponent;
  let fixture: ComponentFixture<DayendModalFormComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DayendModalFormComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DayendModalFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
