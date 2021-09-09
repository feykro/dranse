import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { LigneCommandeDetailComponent } from './ligne-commande-detail.component';

describe('Component Tests', () => {
  describe('LigneCommande Management Detail Component', () => {
    let comp: LigneCommandeDetailComponent;
    let fixture: ComponentFixture<LigneCommandeDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [LigneCommandeDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ ligneCommande: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(LigneCommandeDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(LigneCommandeDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load ligneCommande on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.ligneCommande).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
