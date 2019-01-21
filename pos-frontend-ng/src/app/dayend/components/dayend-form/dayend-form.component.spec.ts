import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DayendFormComponent } from './dayend-form.component';

describe('DayendFormComponent', () => {
  let component: DayendFormComponent;
  let fixture: ComponentFixture<DayendFormComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DayendFormComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DayendFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
