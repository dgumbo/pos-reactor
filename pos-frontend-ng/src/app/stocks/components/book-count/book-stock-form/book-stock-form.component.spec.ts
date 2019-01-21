import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { BookStockFormComponent } from './book-stock-form.component';

describe('BookStockFormComponent', () => {
  let component: BookStockFormComponent;
  let fixture: ComponentFixture<BookStockFormComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ BookStockFormComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BookStockFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
