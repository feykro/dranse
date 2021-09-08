import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CategorieDetailComponent } from './categorie-detail.component';

describe('Component Tests', () => {
  describe('Categorie Management Detail Component', () => {
    let comp: CategorieDetailComponent;
    let fixture: ComponentFixture<CategorieDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [CategorieDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ categorie: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(CategorieDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CategorieDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load categorie on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.categorie).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
