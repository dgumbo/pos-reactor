import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DayendViewComponent } from './dayend-view.component';

describe('DayendViewComponent', () => {
  let component: DayendViewComponent;
  let fixture: ComponentFixture<DayendViewComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DayendViewComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DayendViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
