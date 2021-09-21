jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ILigneCommande, LigneCommande } from '../ligne-commande.model';
import { LigneCommandeService } from '../service/ligne-commande.service';

import { LigneCommandeRoutingResolveService } from './ligne-commande-routing-resolve.service';

describe('Service Tests', () => {
  describe('LigneCommande routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: LigneCommandeRoutingResolveService;
    let service: LigneCommandeService;
    let resultLigneCommande: ILigneCommande | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(LigneCommandeRoutingResolveService);
      service = TestBed.inject(LigneCommandeService);
      resultLigneCommande = undefined;
    });

    describe('resolve', () => {
      it('should return ILigneCommande returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultLigneCommande = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultLigneCommande).toEqual({ id: 123 });
      });

      it('should return new ILigneCommande if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultLigneCommande = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultLigneCommande).toEqual(new LigneCommande());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as LigneCommande })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultLigneCommande = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultLigneCommande).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
