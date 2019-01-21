import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { StockReceiveComponent } from './stock-receive.component';


describe('StockReceiveComponent', () => {
  let component: StockReceiveComponent;
  let fixture: ComponentFixture<StockReceiveComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [StockReceiveComponent]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(StockReceiveComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
