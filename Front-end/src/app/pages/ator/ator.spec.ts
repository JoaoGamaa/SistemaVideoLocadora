import { provideZonelessChangeDetection } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Ator } from './ator';

describe('Ator', () => {
  let component: Ator;
  let fixture: ComponentFixture<Ator>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Ator],
      providers: [provideZonelessChangeDetection()],
    }).compileComponents();

    fixture = TestBed.createComponent(Ator);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
