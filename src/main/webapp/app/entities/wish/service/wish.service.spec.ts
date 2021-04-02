import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { WishCategory } from 'app/entities/enumerations/wish-category.model';
import { IWish, Wish } from '../wish.model';

import { WishService } from './wish.service';

describe('Service Tests', () => {
  describe('Wish Service', () => {
    let service: WishService;
    let httpMock: HttpTestingController;
    let elemDefault: IWish;
    let expectedResult: IWish | IWish[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(WishService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        myWishNote: 'AAAAAAA',
        startDate: currentDate,
        fulfilled: false,
        fulfilledDate: currentDate,
        duration: 0,
        category: WishCategory.MISC,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            startDate: currentDate.format(DATE_FORMAT),
            fulfilledDate: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Wish', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            startDate: currentDate.format(DATE_FORMAT),
            fulfilledDate: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            startDate: currentDate,
            fulfilledDate: currentDate,
          },
          returnedFromService
        );

        service.create(new Wish()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Wish', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            myWishNote: 'BBBBBB',
            startDate: currentDate.format(DATE_FORMAT),
            fulfilled: true,
            fulfilledDate: currentDate.format(DATE_FORMAT),
            duration: 1,
            category: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            startDate: currentDate,
            fulfilledDate: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Wish', () => {
        const patchObject = Object.assign(
          {
            myWishNote: 'BBBBBB',
            startDate: currentDate.format(DATE_FORMAT),
            category: 'BBBBBB',
          },
          new Wish()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            startDate: currentDate,
            fulfilledDate: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Wish', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            myWishNote: 'BBBBBB',
            startDate: currentDate.format(DATE_FORMAT),
            fulfilled: true,
            fulfilledDate: currentDate.format(DATE_FORMAT),
            duration: 1,
            category: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            startDate: currentDate,
            fulfilledDate: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Wish', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addWishToCollectionIfMissing', () => {
        it('should add a Wish to an empty array', () => {
          const wish: IWish = { id: 123 };
          expectedResult = service.addWishToCollectionIfMissing([], wish);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(wish);
        });

        it('should not add a Wish to an array that contains it', () => {
          const wish: IWish = { id: 123 };
          const wishCollection: IWish[] = [
            {
              ...wish,
            },
            { id: 456 },
          ];
          expectedResult = service.addWishToCollectionIfMissing(wishCollection, wish);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Wish to an array that doesn't contain it", () => {
          const wish: IWish = { id: 123 };
          const wishCollection: IWish[] = [{ id: 456 }];
          expectedResult = service.addWishToCollectionIfMissing(wishCollection, wish);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(wish);
        });

        it('should add only unique Wish to an array', () => {
          const wishArray: IWish[] = [{ id: 123 }, { id: 456 }, { id: 96516 }];
          const wishCollection: IWish[] = [{ id: 123 }];
          expectedResult = service.addWishToCollectionIfMissing(wishCollection, ...wishArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const wish: IWish = { id: 123 };
          const wish2: IWish = { id: 456 };
          expectedResult = service.addWishToCollectionIfMissing([], wish, wish2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(wish);
          expect(expectedResult).toContain(wish2);
        });

        it('should accept null and undefined values', () => {
          const wish: IWish = { id: 123 };
          expectedResult = service.addWishToCollectionIfMissing([], null, wish, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(wish);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
