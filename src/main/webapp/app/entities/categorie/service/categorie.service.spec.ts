import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ICategorie, Categorie } from '../categorie.model';

import { CategorieService } from './categorie.service';

describe('Service Tests', () => {
  describe('Categorie Service', () => {
    let service: CategorieService;
    let httpMock: HttpTestingController;
    let elemDefault: ICategorie;
    let expectedResult: ICategorie | ICategorie[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(CategorieService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        nom: 'AAAAAAA',
        description: 'AAAAAAA',
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Categorie', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Categorie()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Categorie', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nom: 'BBBBBB',
            description: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Categorie', () => {
        const patchObject = Object.assign(
          {
            nom: 'BBBBBB',
          },
          new Categorie()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Categorie', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nom: 'BBBBBB',
            description: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Categorie', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addCategorieToCollectionIfMissing', () => {
        it('should add a Categorie to an empty array', () => {
          const categorie: ICategorie = { id: 123 };
          expectedResult = service.addCategorieToCollectionIfMissing([], categorie);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(categorie);
        });

        it('should not add a Categorie to an array that contains it', () => {
          const categorie: ICategorie = { id: 123 };
          const categorieCollection: ICategorie[] = [
            {
              ...categorie,
            },
            { id: 456 },
          ];
          expectedResult = service.addCategorieToCollectionIfMissing(categorieCollection, categorie);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Categorie to an array that doesn't contain it", () => {
          const categorie: ICategorie = { id: 123 };
          const categorieCollection: ICategorie[] = [{ id: 456 }];
          expectedResult = service.addCategorieToCollectionIfMissing(categorieCollection, categorie);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(categorie);
        });

        it('should add only unique Categorie to an array', () => {
          const categorieArray: ICategorie[] = [{ id: 123 }, { id: 456 }, { id: 69056 }];
          const categorieCollection: ICategorie[] = [{ id: 123 }];
          expectedResult = service.addCategorieToCollectionIfMissing(categorieCollection, ...categorieArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const categorie: ICategorie = { id: 123 };
          const categorie2: ICategorie = { id: 456 };
          expectedResult = service.addCategorieToCollectionIfMissing([], categorie, categorie2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(categorie);
          expect(expectedResult).toContain(categorie2);
        });

        it('should accept null and undefined values', () => {
          const categorie: ICategorie = { id: 123 };
          expectedResult = service.addCategorieToCollectionIfMissing([], null, categorie, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(categorie);
        });

        it('should return initial array if no Categorie is added', () => {
          const categorieCollection: ICategorie[] = [{ id: 123 }];
          expectedResult = service.addCategorieToCollectionIfMissing(categorieCollection, undefined, null);
          expect(expectedResult).toEqual(categorieCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
