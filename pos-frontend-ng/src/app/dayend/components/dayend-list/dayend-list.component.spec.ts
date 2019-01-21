import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DayendListComponent } from './dayend-list.component';

describe('DayendListComponent', () => {
  let component: DayendListComponent;
  let fixture: ComponentFixture<DayendListComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DayendListComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DayendListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
