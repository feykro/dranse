jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { UtilisateurService } from '../service/utilisateur.service';
import { IUtilisateur, Utilisateur } from '../utilisateur.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

import { UtilisateurUpdateComponent } from './utilisateur-update.component';

describe('Component Tests', () => {
  describe('Utilisateur Management Update Component', () => {
    let comp: UtilisateurUpdateComponent;
    let fixture: ComponentFixture<UtilisateurUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let utilisateurService: UtilisateurService;
    let userService: UserService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [UtilisateurUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(UtilisateurUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(UtilisateurUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      utilisateurService = TestBed.inject(UtilisateurService);
      userService = TestBed.inject(UserService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call User query and add missing value', () => {
        const utilisateur: IUtilisateur = { id: 456 };
        const user: IUser = { id: 74654 };
        utilisateur.user = user;

        const userCollection: IUser[] = [{ id: 95229 }];
        jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
        const additionalUsers = [user];
        const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
        jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ utilisateur });
        comp.ngOnInit();

        expect(userService.query).toHaveBeenCalled();
        expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
        expect(comp.usersSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const utilisateur: IUtilisateur = { id: 456 };
        const user: IUser = { id: 57517 };
        utilisateur.user = user;

        activatedRoute.data = of({ utilisateur });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(utilisateur));
        expect(comp.usersSharedCollection).toContain(user);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Utilisateur>>();
        const utilisateur = { id: 123 };
        jest.spyOn(utilisateurService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ utilisateur });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: utilisateur }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(utilisateurService.update).toHaveBeenCalledWith(utilisateur);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Utilisateur>>();
        const utilisateur = new Utilisateur();
        jest.spyOn(utilisateurService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ utilisateur });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: utilisateur }));
        saveSubject.complete();

        // THEN
        expect(utilisateurService.create).toHaveBeenCalledWith(utilisateur);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Utilisateur>>();
        const utilisateur = { id: 123 };
        jest.spyOn(utilisateurService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ utilisateur });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(utilisateurService.update).toHaveBeenCalledWith(utilisateur);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackUserById', () => {
        it('Should return tracked User primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackUserById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
