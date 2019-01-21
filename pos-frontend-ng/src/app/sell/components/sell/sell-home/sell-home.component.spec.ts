import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SellHomeComponent } from './sell-home.component';

describe('SellHomeComponent', () => {
  let component: SellHomeComponent;
  let fixture: ComponentFixture<SellHomeComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SellHomeComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SellHomeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
