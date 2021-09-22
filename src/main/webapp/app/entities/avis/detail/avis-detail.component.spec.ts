import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AvisDetailComponent } from './avis-detail.component';

describe('Component Tests', () => {
  describe('Avis Management Detail Component', () => {
    let comp: AvisDetailComponent;
    let fixture: ComponentFixture<AvisDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [AvisDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ avis: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(AvisDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(AvisDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load avis on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.avis).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
