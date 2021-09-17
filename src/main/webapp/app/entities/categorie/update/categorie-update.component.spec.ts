jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CategorieService } from '../service/categorie.service';
import { ICategorie, Categorie } from '../categorie.model';

import { CategorieUpdateComponent } from './categorie-update.component';

describe('Component Tests', () => {
  describe('Categorie Management Update Component', () => {
    let comp: CategorieUpdateComponent;
    let fixture: ComponentFixture<CategorieUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let categorieService: CategorieService;

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

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const categorie: ICategorie = { id: 456 };

        activatedRoute.data = of({ categorie });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(categorie));
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
  });
});
