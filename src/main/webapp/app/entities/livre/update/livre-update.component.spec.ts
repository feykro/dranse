jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { LivreService } from '../service/livre.service';
import { ILivre, Livre } from '../livre.model';
import { ICategorie } from 'app/entities/categorie/categorie.model';
import { CategorieService } from 'app/entities/categorie/service/categorie.service';

import { LivreUpdateComponent } from './livre-update.component';

describe('Component Tests', () => {
  describe('Livre Management Update Component', () => {
    let comp: LivreUpdateComponent;
    let fixture: ComponentFixture<LivreUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let livreService: LivreService;
    let categorieService: CategorieService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [LivreUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(LivreUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(LivreUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      livreService = TestBed.inject(LivreService);
      categorieService = TestBed.inject(CategorieService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Categorie query and add missing value', () => {
        const livre: ILivre = { id: 456 };
        const categories: ICategorie[] = [{ id: 17790 }];
        livre.categories = categories;

        const categorieCollection: ICategorie[] = [{ id: 63543 }];
        jest.spyOn(categorieService, 'query').mockReturnValue(of(new HttpResponse({ body: categorieCollection })));
        const additionalCategories = [...categories];
        const expectedCollection: ICategorie[] = [...additionalCategories, ...categorieCollection];
        jest.spyOn(categorieService, 'addCategorieToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ livre });
        comp.ngOnInit();

        expect(categorieService.query).toHaveBeenCalled();
        expect(categorieService.addCategorieToCollectionIfMissing).toHaveBeenCalledWith(categorieCollection, ...additionalCategories);
        expect(comp.categoriesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const livre: ILivre = { id: 456 };
        const categories: ICategorie = { id: 87656 };
        livre.categories = [categories];

        activatedRoute.data = of({ livre });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(livre));
        expect(comp.categoriesSharedCollection).toContain(categories);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Livre>>();
        const livre = { id: 123 };
        jest.spyOn(livreService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ livre });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: livre }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(livreService.update).toHaveBeenCalledWith(livre);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Livre>>();
        const livre = new Livre();
        jest.spyOn(livreService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ livre });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: livre }));
        saveSubject.complete();

        // THEN
        expect(livreService.create).toHaveBeenCalledWith(livre);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Livre>>();
        const livre = { id: 123 };
        jest.spyOn(livreService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ livre });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(livreService.update).toHaveBeenCalledWith(livre);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackCategorieById', () => {
        it('Should return tracked Categorie primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackCategorieById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });

    describe('Getting selected relationships', () => {
      describe('getSelectedCategorie', () => {
        it('Should return option if no Categorie is selected', () => {
          const option = { id: 123 };
          const result = comp.getSelectedCategorie(option);
          expect(result === option).toEqual(true);
        });

        it('Should return selected Categorie for according option', () => {
          const option = { id: 123 };
          const selected = { id: 123 };
          const selected2 = { id: 456 };
          const result = comp.getSelectedCategorie(option, [selected2, selected]);
          expect(result === selected).toEqual(true);
          expect(result === selected2).toEqual(false);
          expect(result === option).toEqual(false);
        });

        it('Should return option if this Categorie is not selected', () => {
          const option = { id: 123 };
          const selected = { id: 456 };
          const result = comp.getSelectedCategorie(option, [selected]);
          expect(result === option).toEqual(true);
          expect(result === selected).toEqual(false);
        });
      });
    });
  });
});
