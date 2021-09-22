import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { UtilisateurDetailComponent } from './utilisateur-detail.component';

describe('Component Tests', () => {
  describe('Utilisateur Management Detail Component', () => {
    let comp: UtilisateurDetailComponent;
    let fixture: ComponentFixture<UtilisateurDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [UtilisateurDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ utilisateur: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(UtilisateurDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(UtilisateurDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load utilisateur on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.utilisateur).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
