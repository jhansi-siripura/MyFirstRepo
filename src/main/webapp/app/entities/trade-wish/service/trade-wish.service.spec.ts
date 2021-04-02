import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ITradeWish, TradeWish } from '../trade-wish.model';

import { TradeWishService } from './trade-wish.service';

describe('Service Tests', () => {
  describe('TradeWish Service', () => {
    let service: TradeWishService;
    let httpMock: HttpTestingController;
    let elemDefault: ITradeWish;
    let expectedResult: ITradeWish | ITradeWish[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(TradeWishService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        twish: 0,
        picked: false,
        pickedDate: currentDate,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            pickedDate: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a TradeWish', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            pickedDate: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            pickedDate: currentDate,
          },
          returnedFromService
        );

        service.create(new TradeWish()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a TradeWish', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            twish: 1,
            picked: true,
            pickedDate: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            pickedDate: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a TradeWish', () => {
        const patchObject = Object.assign(
          {
            twish: 1,
            pickedDate: currentDate.format(DATE_TIME_FORMAT),
          },
          new TradeWish()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            pickedDate: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of TradeWish', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            twish: 1,
            picked: true,
            pickedDate: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            pickedDate: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a TradeWish', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addTradeWishToCollectionIfMissing', () => {
        it('should add a TradeWish to an empty array', () => {
          const tradeWish: ITradeWish = { id: 123 };
          expectedResult = service.addTradeWishToCollectionIfMissing([], tradeWish);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(tradeWish);
        });

        it('should not add a TradeWish to an array that contains it', () => {
          const tradeWish: ITradeWish = { id: 123 };
          const tradeWishCollection: ITradeWish[] = [
            {
              ...tradeWish,
            },
            { id: 456 },
          ];
          expectedResult = service.addTradeWishToCollectionIfMissing(tradeWishCollection, tradeWish);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a TradeWish to an array that doesn't contain it", () => {
          const tradeWish: ITradeWish = { id: 123 };
          const tradeWishCollection: ITradeWish[] = [{ id: 456 }];
          expectedResult = service.addTradeWishToCollectionIfMissing(tradeWishCollection, tradeWish);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(tradeWish);
        });

        it('should add only unique TradeWish to an array', () => {
          const tradeWishArray: ITradeWish[] = [{ id: 123 }, { id: 456 }, { id: 91447 }];
          const tradeWishCollection: ITradeWish[] = [{ id: 123 }];
          expectedResult = service.addTradeWishToCollectionIfMissing(tradeWishCollection, ...tradeWishArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const tradeWish: ITradeWish = { id: 123 };
          const tradeWish2: ITradeWish = { id: 456 };
          expectedResult = service.addTradeWishToCollectionIfMissing([], tradeWish, tradeWish2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(tradeWish);
          expect(expectedResult).toContain(tradeWish2);
        });

        it('should accept null and undefined values', () => {
          const tradeWish: ITradeWish = { id: 123 };
          expectedResult = service.addTradeWishToCollectionIfMissing([], null, tradeWish, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(tradeWish);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
