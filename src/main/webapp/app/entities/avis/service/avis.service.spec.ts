import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IAvis, Avis } from '../avis.model';

import { AvisService } from './avis.service';

describe('Service Tests', () => {
  describe('Avis Service', () => {
    let service: AvisService;
    let httpMock: HttpTestingController;
    let elemDefault: IAvis;
    let expectedResult: IAvis | IAvis[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(AvisService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        note: 0,
        commentaire: 'AAAAAAA',
        datePublication: currentDate,
        affiche: false,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            datePublication: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Avis', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            datePublication: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            datePublication: currentDate,
          },
          returnedFromService
        );

        service.create(new Avis()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Avis', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            note: 1,
            commentaire: 'BBBBBB',
            datePublication: currentDate.format(DATE_TIME_FORMAT),
            affiche: true,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            datePublication: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Avis', () => {
        const patchObject = Object.assign(
          {
            note: 1,
            commentaire: 'BBBBBB',
            datePublication: currentDate.format(DATE_TIME_FORMAT),
          },
          new Avis()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            datePublication: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Avis', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            note: 1,
            commentaire: 'BBBBBB',
            datePublication: currentDate.format(DATE_TIME_FORMAT),
            affiche: true,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            datePublication: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Avis', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addAvisToCollectionIfMissing', () => {
        it('should add a Avis to an empty array', () => {
          const avis: IAvis = { id: 123 };
          expectedResult = service.addAvisToCollectionIfMissing([], avis);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(avis);
        });

        it('should not add a Avis to an array that contains it', () => {
          const avis: IAvis = { id: 123 };
          const avisCollection: IAvis[] = [
            {
              ...avis,
            },
            { id: 456 },
          ];
          expectedResult = service.addAvisToCollectionIfMissing(avisCollection, avis);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Avis to an array that doesn't contain it", () => {
          const avis: IAvis = { id: 123 };
          const avisCollection: IAvis[] = [{ id: 456 }];
          expectedResult = service.addAvisToCollectionIfMissing(avisCollection, avis);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(avis);
        });

        it('should add only unique Avis to an array', () => {
          const avisArray: IAvis[] = [{ id: 123 }, { id: 456 }, { id: 31755 }];
          const avisCollection: IAvis[] = [{ id: 123 }];
          expectedResult = service.addAvisToCollectionIfMissing(avisCollection, ...avisArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const avis: IAvis = { id: 123 };
          const avis2: IAvis = { id: 456 };
          expectedResult = service.addAvisToCollectionIfMissing([], avis, avis2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(avis);
          expect(expectedResult).toContain(avis2);
        });

        it('should accept null and undefined values', () => {
          const avis: IAvis = { id: 123 };
          expectedResult = service.addAvisToCollectionIfMissing([], null, avis, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(avis);
        });

        it('should return initial array if no Avis is added', () => {
          const avisCollection: IAvis[] = [{ id: 123 }];
          expectedResult = service.addAvisToCollectionIfMissing(avisCollection, undefined, null);
          expect(expectedResult).toEqual(avisCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
