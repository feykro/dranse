import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FormspaiementComponent } from './formspaiement.component';

describe('FormspaiementComponent', () => {
  let component: FormspaiementComponent;
  let fixture: ComponentFixture<FormspaiementComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [FormspaiementComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(FormspaiementComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
