jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ICategorie, Categorie } from '../categorie.model';
import { CategorieService } from '../service/categorie.service';

import { CategorieRoutingResolveService } from './categorie-routing-resolve.service';

describe('Service Tests', () => {
  describe('Categorie routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: CategorieRoutingResolveService;
    let service: CategorieService;
    let resultCategorie: ICategorie | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(CategorieRoutingResolveService);
      service = TestBed.inject(CategorieService);
      resultCategorie = undefined;
    });

    describe('resolve', () => {
      it('should return ICategorie returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCategorie = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultCategorie).toEqual({ id: 123 });
      });

      it('should return new ICategorie if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCategorie = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultCategorie).toEqual(new Categorie());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Categorie })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCategorie = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultCategorie).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
