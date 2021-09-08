jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IAvis, Avis } from '../avis.model';
import { AvisService } from '../service/avis.service';

import { AvisRoutingResolveService } from './avis-routing-resolve.service';

describe('Service Tests', () => {
  describe('Avis routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: AvisRoutingResolveService;
    let service: AvisService;
    let resultAvis: IAvis | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(AvisRoutingResolveService);
      service = TestBed.inject(AvisService);
      resultAvis = undefined;
    });

    describe('resolve', () => {
      it('should return IAvis returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultAvis = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultAvis).toEqual({ id: 123 });
      });

      it('should return new IAvis if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultAvis = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultAvis).toEqual(new Avis());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Avis })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultAvis = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultAvis).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
