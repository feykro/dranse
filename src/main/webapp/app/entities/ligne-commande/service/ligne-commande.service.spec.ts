import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ILigneCommande, LigneCommande } from '../ligne-commande.model';

import { LigneCommandeService } from './ligne-commande.service';

describe('Service Tests', () => {
  describe('LigneCommande Service', () => {
    let service: LigneCommandeService;
    let httpMock: HttpTestingController;
    let elemDefault: ILigneCommande;
    let expectedResult: ILigneCommande | ILigneCommande[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(LigneCommandeService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        quantite: 0,
        prixPaye: 0,
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

      it('should create a LigneCommande', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new LigneCommande()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a LigneCommande', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            quantite: 1,
            prixPaye: 1,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a LigneCommande', () => {
        const patchObject = Object.assign(
          {
            quantite: 1,
          },
          new LigneCommande()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of LigneCommande', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            quantite: 1,
            prixPaye: 1,
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

      it('should delete a LigneCommande', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addLigneCommandeToCollectionIfMissing', () => {
        it('should add a LigneCommande to an empty array', () => {
          const ligneCommande: ILigneCommande = { id: 123 };
          expectedResult = service.addLigneCommandeToCollectionIfMissing([], ligneCommande);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(ligneCommande);
        });

        it('should not add a LigneCommande to an array that contains it', () => {
          const ligneCommande: ILigneCommande = { id: 123 };
          const ligneCommandeCollection: ILigneCommande[] = [
            {
              ...ligneCommande,
            },
            { id: 456 },
          ];
          expectedResult = service.addLigneCommandeToCollectionIfMissing(ligneCommandeCollection, ligneCommande);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a LigneCommande to an array that doesn't contain it", () => {
          const ligneCommande: ILigneCommande = { id: 123 };
          const ligneCommandeCollection: ILigneCommande[] = [{ id: 456 }];
          expectedResult = service.addLigneCommandeToCollectionIfMissing(ligneCommandeCollection, ligneCommande);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(ligneCommande);
        });

        it('should add only unique LigneCommande to an array', () => {
          const ligneCommandeArray: ILigneCommande[] = [{ id: 123 }, { id: 456 }, { id: 13776 }];
          const ligneCommandeCollection: ILigneCommande[] = [{ id: 123 }];
          expectedResult = service.addLigneCommandeToCollectionIfMissing(ligneCommandeCollection, ...ligneCommandeArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const ligneCommande: ILigneCommande = { id: 123 };
          const ligneCommande2: ILigneCommande = { id: 456 };
          expectedResult = service.addLigneCommandeToCollectionIfMissing([], ligneCommande, ligneCommande2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(ligneCommande);
          expect(expectedResult).toContain(ligneCommande2);
        });

        it('should accept null and undefined values', () => {
          const ligneCommande: ILigneCommande = { id: 123 };
          expectedResult = service.addLigneCommandeToCollectionIfMissing([], null, ligneCommande, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(ligneCommande);
        });

        it('should return initial array if no LigneCommande is added', () => {
          const ligneCommandeCollection: ILigneCommande[] = [{ id: 123 }];
          expectedResult = service.addLigneCommandeToCollectionIfMissing(ligneCommandeCollection, undefined, null);
          expect(expectedResult).toEqual(ligneCommandeCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
