jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { LigneCommandeService } from '../service/ligne-commande.service';
import { ILigneCommande, LigneCommande } from '../ligne-commande.model';
import { ICommande } from 'app/entities/commande/commande.model';
import { CommandeService } from 'app/entities/commande/service/commande.service';
import { ILivre } from 'app/entities/livre/livre.model';
import { LivreService } from 'app/entities/livre/service/livre.service';

import { LigneCommandeUpdateComponent } from './ligne-commande-update.component';

describe('Component Tests', () => {
  describe('LigneCommande Management Update Component', () => {
    let comp: LigneCommandeUpdateComponent;
    let fixture: ComponentFixture<LigneCommandeUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let ligneCommandeService: LigneCommandeService;
    let commandeService: CommandeService;
    let livreService: LivreService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [LigneCommandeUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(LigneCommandeUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(LigneCommandeUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      ligneCommandeService = TestBed.inject(LigneCommandeService);
      commandeService = TestBed.inject(CommandeService);
      livreService = TestBed.inject(LivreService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Commande query and add missing value', () => {
        const ligneCommande: ILigneCommande = { id: 456 };
        const commande: ICommande = { id: 7874 };
        ligneCommande.commande = commande;

        const commandeCollection: ICommande[] = [{ id: 78715 }];
        jest.spyOn(commandeService, 'query').mockReturnValue(of(new HttpResponse({ body: commandeCollection })));
        const additionalCommandes = [commande];
        const expectedCollection: ICommande[] = [...additionalCommandes, ...commandeCollection];
        jest.spyOn(commandeService, 'addCommandeToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ ligneCommande });
        comp.ngOnInit();

        expect(commandeService.query).toHaveBeenCalled();
        expect(commandeService.addCommandeToCollectionIfMissing).toHaveBeenCalledWith(commandeCollection, ...additionalCommandes);
        expect(comp.commandesSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Livre query and add missing value', () => {
        const ligneCommande: ILigneCommande = { id: 456 };
        const livre: ILivre = { id: 75709 };
        ligneCommande.livre = livre;

        const livreCollection: ILivre[] = [{ id: 35801 }];
        jest.spyOn(livreService, 'query').mockReturnValue(of(new HttpResponse({ body: livreCollection })));
        const additionalLivres = [livre];
        const expectedCollection: ILivre[] = [...additionalLivres, ...livreCollection];
        jest.spyOn(livreService, 'addLivreToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ ligneCommande });
        comp.ngOnInit();

        expect(livreService.query).toHaveBeenCalled();
        expect(livreService.addLivreToCollectionIfMissing).toHaveBeenCalledWith(livreCollection, ...additionalLivres);
        expect(comp.livresSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const ligneCommande: ILigneCommande = { id: 456 };
        const commande: ICommande = { id: 45253 };
        ligneCommande.commande = commande;
        const livre: ILivre = { id: 84281 };
        ligneCommande.livre = livre;

        activatedRoute.data = of({ ligneCommande });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(ligneCommande));
        expect(comp.commandesSharedCollection).toContain(commande);
        expect(comp.livresSharedCollection).toContain(livre);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<LigneCommande>>();
        const ligneCommande = { id: 123 };
        jest.spyOn(ligneCommandeService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ ligneCommande });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: ligneCommande }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(ligneCommandeService.update).toHaveBeenCalledWith(ligneCommande);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<LigneCommande>>();
        const ligneCommande = new LigneCommande();
        jest.spyOn(ligneCommandeService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ ligneCommande });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: ligneCommande }));
        saveSubject.complete();

        // THEN
        expect(ligneCommandeService.create).toHaveBeenCalledWith(ligneCommande);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<LigneCommande>>();
        const ligneCommande = { id: 123 };
        jest.spyOn(ligneCommandeService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ ligneCommande });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(ligneCommandeService.update).toHaveBeenCalledWith(ligneCommande);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackCommandeById', () => {
        it('Should return tracked Commande primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackCommandeById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackLivreById', () => {
        it('Should return tracked Livre primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackLivreById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
