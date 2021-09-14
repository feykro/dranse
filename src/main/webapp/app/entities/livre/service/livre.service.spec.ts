import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ILivre, Livre } from '../livre.model';

import { LivreService } from './livre.service';

describe('Service Tests', () => {
  describe('Livre Service', () => {
    let service: LivreService;
    let httpMock: HttpTestingController;
    let elemDefault: ILivre;
    let expectedResult: ILivre | ILivre[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(LivreService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        titre: 'AAAAAAA',
        auteur: 'AAAAAAA',
        prix: 0,
        synopsis: 'AAAAAAA',
        edition: 0,
        anneePublication: 0,
        editeur: 'AAAAAAA',
        stock: 0,
        urlImage: 'AAAAAAA',
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

      it('should create a Livre', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Livre()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Livre', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            titre: 'BBBBBB',
            auteur: 'BBBBBB',
            prix: 1,
            synopsis: 'BBBBBB',
            edition: 1,
            anneePublication: 1,
            editeur: 'BBBBBB',
            stock: 1,
            urlImage: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Livre', () => {
        const patchObject = Object.assign(
          {
            auteur: 'BBBBBB',
            anneePublication: 1,
            stock: 1,
            urlImage: 'BBBBBB',
          },
          new Livre()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Livre', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            titre: 'BBBBBB',
            auteur: 'BBBBBB',
            prix: 1,
            synopsis: 'BBBBBB',
            edition: 1,
            anneePublication: 1,
            editeur: 'BBBBBB',
            stock: 1,
            urlImage: 'BBBBBB',
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

      it('should delete a Livre', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addLivreToCollectionIfMissing', () => {
        it('should add a Livre to an empty array', () => {
          const livre: ILivre = { id: 123 };
          expectedResult = service.addLivreToCollectionIfMissing([], livre);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(livre);
        });

        it('should not add a Livre to an array that contains it', () => {
          const livre: ILivre = { id: 123 };
          const livreCollection: ILivre[] = [
            {
              ...livre,
            },
            { id: 456 },
          ];
          expectedResult = service.addLivreToCollectionIfMissing(livreCollection, livre);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Livre to an array that doesn't contain it", () => {
          const livre: ILivre = { id: 123 };
          const livreCollection: ILivre[] = [{ id: 456 }];
          expectedResult = service.addLivreToCollectionIfMissing(livreCollection, livre);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(livre);
        });

        it('should add only unique Livre to an array', () => {
          const livreArray: ILivre[] = [{ id: 123 }, { id: 456 }, { id: 72951 }];
          const livreCollection: ILivre[] = [{ id: 123 }];
          expectedResult = service.addLivreToCollectionIfMissing(livreCollection, ...livreArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const livre: ILivre = { id: 123 };
          const livre2: ILivre = { id: 456 };
          expectedResult = service.addLivreToCollectionIfMissing([], livre, livre2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(livre);
          expect(expectedResult).toContain(livre2);
        });

        it('should accept null and undefined values', () => {
          const livre: ILivre = { id: 123 };
          expectedResult = service.addLivreToCollectionIfMissing([], null, livre, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(livre);
        });

        it('should return initial array if no Livre is added', () => {
          const livreCollection: ILivre[] = [{ id: 123 }];
          expectedResult = service.addLivreToCollectionIfMissing(livreCollection, undefined, null);
          expect(expectedResult).toEqual(livreCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
