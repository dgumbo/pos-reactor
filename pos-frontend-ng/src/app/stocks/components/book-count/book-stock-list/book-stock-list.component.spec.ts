import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { BookStockListComponent } from './book-stock-list.component';

describe('BookStockListComponent', () => {
  let component: BookStockListComponent;
  let fixture: ComponentFixture<BookStockListComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ BookStockListComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BookStockListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
