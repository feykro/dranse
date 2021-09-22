import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ConfirmationachatComponent } from './confirmationachat.component';

describe('ConfirmationachatComponent', () => {
  let component: ConfirmationachatComponent;
  let fixture: ComponentFixture<ConfirmationachatComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ConfirmationachatComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ConfirmationachatComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
