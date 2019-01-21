import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RoleUnitSelectComponent } from './role-unit-select.component';

describe('RoleUnitSelectComponent', () => {
  let component: RoleUnitSelectComponent;
  let fixture: ComponentFixture<RoleUnitSelectComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RoleUnitSelectComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RoleUnitSelectComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
