jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { AvisService } from '../service/avis.service';
import { IAvis, Avis } from '../avis.model';
import { ILivre } from 'app/entities/livre/livre.model';
import { LivreService } from 'app/entities/livre/service/livre.service';
import { IUtilisateur } from 'app/entities/utilisateur/utilisateur.model';
import { UtilisateurService } from 'app/entities/utilisateur/service/utilisateur.service';

import { AvisUpdateComponent } from './avis-update.component';

describe('Component Tests', () => {
  describe('Avis Management Update Component', () => {
    let comp: AvisUpdateComponent;
    let fixture: ComponentFixture<AvisUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let avisService: AvisService;
    let livreService: LivreService;
    let utilisateurService: UtilisateurService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [AvisUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(AvisUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(AvisUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      avisService = TestBed.inject(AvisService);
      livreService = TestBed.inject(LivreService);
      utilisateurService = TestBed.inject(UtilisateurService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Livre query and add missing value', () => {
        const avis: IAvis = { id: 456 };
        const livre: ILivre = { id: 64823 };
        avis.livre = livre;

        const livreCollection: ILivre[] = [{ id: 85311 }];
        jest.spyOn(livreService, 'query').mockReturnValue(of(new HttpResponse({ body: livreCollection })));
        const additionalLivres = [livre];
        const expectedCollection: ILivre[] = [...additionalLivres, ...livreCollection];
        jest.spyOn(livreService, 'addLivreToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ avis });
        comp.ngOnInit();

        expect(livreService.query).toHaveBeenCalled();
        expect(livreService.addLivreToCollectionIfMissing).toHaveBeenCalledWith(livreCollection, ...additionalLivres);
        expect(comp.livresSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Utilisateur query and add missing value', () => {
        const avis: IAvis = { id: 456 };
        const utilisateur: IUtilisateur = { id: 60388 };
        avis.utilisateur = utilisateur;

        const utilisateurCollection: IUtilisateur[] = [{ id: 59066 }];
        jest.spyOn(utilisateurService, 'query').mockReturnValue(of(new HttpResponse({ body: utilisateurCollection })));
        const additionalUtilisateurs = [utilisateur];
        const expectedCollection: IUtilisateur[] = [...additionalUtilisateurs, ...utilisateurCollection];
        jest.spyOn(utilisateurService, 'addUtilisateurToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ avis });
        comp.ngOnInit();

        expect(utilisateurService.query).toHaveBeenCalled();
        expect(utilisateurService.addUtilisateurToCollectionIfMissing).toHaveBeenCalledWith(
          utilisateurCollection,
          ...additionalUtilisateurs
        );
        expect(comp.utilisateursSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const avis: IAvis = { id: 456 };
        const livre: ILivre = { id: 69344 };
        avis.livre = livre;
        const utilisateur: IUtilisateur = { id: 8306 };
        avis.utilisateur = utilisateur;

        activatedRoute.data = of({ avis });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(avis));
        expect(comp.livresSharedCollection).toContain(livre);
        expect(comp.utilisateursSharedCollection).toContain(utilisateur);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Avis>>();
        const avis = { id: 123 };
        jest.spyOn(avisService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ avis });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: avis }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(avisService.update).toHaveBeenCalledWith(avis);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Avis>>();
        const avis = new Avis();
        jest.spyOn(avisService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ avis });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: avis }));
        saveSubject.complete();

        // THEN
        expect(avisService.create).toHaveBeenCalledWith(avis);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Avis>>();
        const avis = { id: 123 };
        jest.spyOn(avisService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ avis });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(avisService.update).toHaveBeenCalledWith(avis);
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

      describe('trackUtilisateurById', () => {
        it('Should return tracked Utilisateur primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackUtilisateurById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
