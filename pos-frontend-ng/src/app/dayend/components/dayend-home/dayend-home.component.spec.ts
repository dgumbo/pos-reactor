import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DayendHomeComponent } from './dayend-home.component';

describe('DayendHomeComponent', () => {
  let component: DayendHomeComponent;
  let fixture: ComponentFixture<DayendHomeComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DayendHomeComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DayendHomeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
