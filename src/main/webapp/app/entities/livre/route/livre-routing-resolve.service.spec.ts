jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ILivre, Livre } from '../livre.model';
import { LivreService } from '../service/livre.service';

import { LivreRoutingResolveService } from './livre-routing-resolve.service';

describe('Service Tests', () => {
  describe('Livre routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: LivreRoutingResolveService;
    let service: LivreService;
    let resultLivre: ILivre | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(LivreRoutingResolveService);
      service = TestBed.inject(LivreService);
      resultLivre = undefined;
    });

    describe('resolve', () => {
      it('should return ILivre returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultLivre = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultLivre).toEqual({ id: 123 });
      });

      it('should return new ILivre if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultLivre = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultLivre).toEqual(new Livre());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Livre })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultLivre = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultLivre).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
