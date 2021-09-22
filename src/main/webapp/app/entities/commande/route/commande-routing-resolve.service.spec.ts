jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ICommande, Commande } from '../commande.model';
import { CommandeService } from '../service/commande.service';

import { CommandeRoutingResolveService } from './commande-routing-resolve.service';

describe('Service Tests', () => {
  describe('Commande routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: CommandeRoutingResolveService;
    let service: CommandeService;
    let resultCommande: ICommande | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(CommandeRoutingResolveService);
      service = TestBed.inject(CommandeService);
      resultCommande = undefined;
    });

    describe('resolve', () => {
      it('should return ICommande returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCommande = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultCommande).toEqual({ id: 123 });
      });

      it('should return new ICommande if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCommande = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultCommande).toEqual(new Commande());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Commande })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCommande = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultCommande).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
