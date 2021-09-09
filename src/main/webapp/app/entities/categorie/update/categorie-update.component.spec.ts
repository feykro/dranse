jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CategorieService } from '../service/categorie.service';
import { ICategorie, Categorie } from '../categorie.model';
import { ILivre } from 'app/entities/livre/livre.model';
import { LivreService } from 'app/entities/livre/service/livre.service';

import { CategorieUpdateComponent } from './categorie-update.component';

describe('Component Tests', () => {
  describe('Categorie Management Update Component', () => {
    let comp: CategorieUpdateComponent;
    let fixture: ComponentFixture<CategorieUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let categorieService: CategorieService;
    let livreService: LivreService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [CategorieUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(CategorieUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CategorieUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      categorieService = TestBed.inject(CategorieService);
      livreService = TestBed.inject(LivreService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Livre query and add missing value', () => {
        const categorie: ICategorie = { id: 456 };
        const livres: ILivre[] = [{ id: 31152 }];
        categorie.livres = livres;

        const livreCollection: ILivre[] = [{ id: 55079 }];
        jest.spyOn(livreService, 'query').mockReturnValue(of(new HttpResponse({ body: livreCollection })));
        const additionalLivres = [...livres];
        const expectedCollection: ILivre[] = [...additionalLivres, ...livreCollection];
        jest.spyOn(livreService, 'addLivreToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ categorie });
        comp.ngOnInit();

        expect(livreService.query).toHaveBeenCalled();
        expect(livreService.addLivreToCollectionIfMissing).toHaveBeenCalledWith(livreCollection, ...additionalLivres);
        expect(comp.livresSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const categorie: ICategorie = { id: 456 };
        const livres: ILivre = { id: 57778 };
        categorie.livres = [livres];

        activatedRoute.data = of({ categorie });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(categorie));
        expect(comp.livresSharedCollection).toContain(livres);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Categorie>>();
        const categorie = { id: 123 };
        jest.spyOn(categorieService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ categorie });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: categorie }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(categorieService.update).toHaveBeenCalledWith(categorie);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Categorie>>();
        const categorie = new Categorie();
        jest.spyOn(categorieService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ categorie });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: categorie }));
        saveSubject.complete();

        // THEN
        expect(categorieService.create).toHaveBeenCalledWith(categorie);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Categorie>>();
        const categorie = { id: 123 };
        jest.spyOn(categorieService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ categorie });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(categorieService.update).toHaveBeenCalledWith(categorie);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackLivreById', () => {
        it('Should return tracked Livre primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackLivreById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });

    describe('Getting selected relationships', () => {
      describe('getSelectedLivre', () => {
        it('Should return option if no Livre is selected', () => {
          const option = { id: 123 };
          const result = comp.getSelectedLivre(option);
          expect(result === option).toEqual(true);
        });

        it('Should return selected Livre for according option', () => {
          const option = { id: 123 };
          const selected = { id: 123 };
          const selected2 = { id: 456 };
          const result = comp.getSelectedLivre(option, [selected2, selected]);
          expect(result === selected).toEqual(true);
          expect(result === selected2).toEqual(false);
          expect(result === option).toEqual(false);
        });

        it('Should return option if this Livre is not selected', () => {
          const option = { id: 123 };
          const selected = { id: 456 };
          const result = comp.getSelectedLivre(option, [selected]);
          expect(result === option).toEqual(true);
          expect(result === selected).toEqual(false);
        });
      });
    });
  });
});
