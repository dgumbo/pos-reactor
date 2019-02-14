import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ReportsPdfPrintPreviewComponent } from './reports-pdf-print-preview.component';

describe('ReportsPdfPrintPreviewComponent', () => {
  let component: ReportsPdfPrintPreviewComponent;
  let fixture: ComponentFixture<ReportsPdfPrintPreviewComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ReportsPdfPrintPreviewComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ReportsPdfPrintPreviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
