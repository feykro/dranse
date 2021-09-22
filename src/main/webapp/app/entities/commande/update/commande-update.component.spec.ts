jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CommandeService } from '../service/commande.service';
import { ICommande, Commande } from '../commande.model';
import { IUtilisateur } from 'app/entities/utilisateur/utilisateur.model';
import { UtilisateurService } from 'app/entities/utilisateur/service/utilisateur.service';

import { CommandeUpdateComponent } from './commande-update.component';

describe('Component Tests', () => {
  describe('Commande Management Update Component', () => {
    let comp: CommandeUpdateComponent;
    let fixture: ComponentFixture<CommandeUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let commandeService: CommandeService;
    let utilisateurService: UtilisateurService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [CommandeUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(CommandeUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CommandeUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      commandeService = TestBed.inject(CommandeService);
      utilisateurService = TestBed.inject(UtilisateurService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Utilisateur query and add missing value', () => {
        const commande: ICommande = { id: 456 };
        const utilisateur: IUtilisateur = { id: 89234 };
        commande.utilisateur = utilisateur;

        const utilisateurCollection: IUtilisateur[] = [{ id: 81087 }];
        jest.spyOn(utilisateurService, 'query').mockReturnValue(of(new HttpResponse({ body: utilisateurCollection })));
        const additionalUtilisateurs = [utilisateur];
        const expectedCollection: IUtilisateur[] = [...additionalUtilisateurs, ...utilisateurCollection];
        jest.spyOn(utilisateurService, 'addUtilisateurToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ commande });
        comp.ngOnInit();

        expect(utilisateurService.query).toHaveBeenCalled();
        expect(utilisateurService.addUtilisateurToCollectionIfMissing).toHaveBeenCalledWith(
          utilisateurCollection,
          ...additionalUtilisateurs
        );
        expect(comp.utilisateursSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const commande: ICommande = { id: 456 };
        const utilisateur: IUtilisateur = { id: 56964 };
        commande.utilisateur = utilisateur;

        activatedRoute.data = of({ commande });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(commande));
        expect(comp.utilisateursSharedCollection).toContain(utilisateur);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Commande>>();
        const commande = { id: 123 };
        jest.spyOn(commandeService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ commande });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: commande }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(commandeService.update).toHaveBeenCalledWith(commande);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Commande>>();
        const commande = new Commande();
        jest.spyOn(commandeService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ commande });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: commande }));
        saveSubject.complete();

        // THEN
        expect(commandeService.create).toHaveBeenCalledWith(commande);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Commande>>();
        const commande = { id: 123 };
        jest.spyOn(commandeService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ commande });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(commandeService.update).toHaveBeenCalledWith(commande);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
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
