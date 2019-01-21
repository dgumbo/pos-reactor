import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ReportAnalysisSheetConfigComponent } from './report-analysis-sheet-config.component';

describe('ReportAnalysisSheetConfigComponent', () => {
  let component: ReportAnalysisSheetConfigComponent;
  let fixture: ComponentFixture<ReportAnalysisSheetConfigComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ReportAnalysisSheetConfigComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ReportAnalysisSheetConfigComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
