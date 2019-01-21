import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { StaticReportViewComponent } from './static-report-view.component';

describe('StaticReportViewComponent', () => {
  let component: StaticReportViewComponent;
  let fixture: ComponentFixture<StaticReportViewComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ StaticReportViewComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(StaticReportViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
