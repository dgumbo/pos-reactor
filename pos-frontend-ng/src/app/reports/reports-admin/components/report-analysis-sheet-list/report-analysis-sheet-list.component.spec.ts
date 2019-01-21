import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ReportAnalysisSheetListComponent } from './report-analysis-sheet-list.component';

describe('ReportAnalysisSheetListComponent', () => {
  let component: ReportAnalysisSheetListComponent;
  let fixture: ComponentFixture<ReportAnalysisSheetListComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ReportAnalysisSheetListComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ReportAnalysisSheetListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
