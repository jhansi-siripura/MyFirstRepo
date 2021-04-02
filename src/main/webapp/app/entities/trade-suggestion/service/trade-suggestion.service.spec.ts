import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT, DATE_TIME_FORMAT } from 'app/config/input.constants';
import { TradeAction } from 'app/entities/enumerations/trade-action.model';
import { TradeStatus } from 'app/entities/enumerations/trade-status.model';
import { TradeResult } from 'app/entities/enumerations/trade-result.model';
import { TradeType } from 'app/entities/enumerations/trade-type.model';
import { ITradeSuggestion, TradeSuggestion } from '../trade-suggestion.model';

import { TradeSuggestionService } from './trade-suggestion.service';

describe('Service Tests', () => {
  describe('TradeSuggestion Service', () => {
    let service: TradeSuggestionService;
    let httpMock: HttpTestingController;
    let elemDefault: ITradeSuggestion;
    let expectedResult: ITradeSuggestion | ITradeSuggestion[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(TradeSuggestionService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        action: TradeAction.BUY,
        tradeInPrice: 0,
        minTradeOutPrice: 0,
        minProfitPoints: 0,
        betterTradeoutPrice: 0,
        betterTradeOutProfitPoints: 0,
        actualTradeoutPrice: 0,
        actualProfitPoints: 0,
        slPoints: 0,
        tradeStatus: TradeStatus.PENDING,
        tradeResults: TradeResult.SUCCESS,
        tradeInTime: currentDate,
        tradeOutTime: currentDate,
        tradeDuration: 0,
        tradeDate: currentDate,
        tradeSuggestionTime: currentDate,
        tradeType: TradeType.NRML,
        actualPL: 0,
        slPrice: 0,
        currentMarketPrice: 0,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            tradeInTime: currentDate.format(DATE_TIME_FORMAT),
            tradeOutTime: currentDate.format(DATE_TIME_FORMAT),
            tradeDate: currentDate.format(DATE_FORMAT),
            tradeSuggestionTime: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a TradeSuggestion', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            tradeInTime: currentDate.format(DATE_TIME_FORMAT),
            tradeOutTime: currentDate.format(DATE_TIME_FORMAT),
            tradeDate: currentDate.format(DATE_FORMAT),
            tradeSuggestionTime: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            tradeInTime: currentDate,
            tradeOutTime: currentDate,
            tradeDate: currentDate,
            tradeSuggestionTime: currentDate,
          },
          returnedFromService
        );

        service.create(new TradeSuggestion()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a TradeSuggestion', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            action: 'BBBBBB',
            tradeInPrice: 1,
            minTradeOutPrice: 1,
            minProfitPoints: 1,
            betterTradeoutPrice: 1,
            betterTradeOutProfitPoints: 1,
            actualTradeoutPrice: 1,
            actualProfitPoints: 1,
            slPoints: 1,
            tradeStatus: 'BBBBBB',
            tradeResults: 'BBBBBB',
            tradeInTime: currentDate.format(DATE_TIME_FORMAT),
            tradeOutTime: currentDate.format(DATE_TIME_FORMAT),
            tradeDuration: 1,
            tradeDate: currentDate.format(DATE_FORMAT),
            tradeSuggestionTime: currentDate.format(DATE_TIME_FORMAT),
            tradeType: 'BBBBBB',
            actualPL: 1,
            slPrice: 1,
            currentMarketPrice: 1,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            tradeInTime: currentDate,
            tradeOutTime: currentDate,
            tradeDate: currentDate,
            tradeSuggestionTime: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a TradeSuggestion', () => {
        const patchObject = Object.assign(
          {
            action: 'BBBBBB',
            tradeInPrice: 1,
            minTradeOutPrice: 1,
            minProfitPoints: 1,
            actualTradeoutPrice: 1,
            actualProfitPoints: 1,
            slPoints: 1,
            tradeStatus: 'BBBBBB',
            tradeResults: 'BBBBBB',
            tradeInTime: currentDate.format(DATE_TIME_FORMAT),
            tradeOutTime: currentDate.format(DATE_TIME_FORMAT),
            tradeDuration: 1,
            tradeType: 'BBBBBB',
            actualPL: 1,
            slPrice: 1,
          },
          new TradeSuggestion()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            tradeInTime: currentDate,
            tradeOutTime: currentDate,
            tradeDate: currentDate,
            tradeSuggestionTime: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of TradeSuggestion', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            action: 'BBBBBB',
            tradeInPrice: 1,
            minTradeOutPrice: 1,
            minProfitPoints: 1,
            betterTradeoutPrice: 1,
            betterTradeOutProfitPoints: 1,
            actualTradeoutPrice: 1,
            actualProfitPoints: 1,
            slPoints: 1,
            tradeStatus: 'BBBBBB',
            tradeResults: 'BBBBBB',
            tradeInTime: currentDate.format(DATE_TIME_FORMAT),
            tradeOutTime: currentDate.format(DATE_TIME_FORMAT),
            tradeDuration: 1,
            tradeDate: currentDate.format(DATE_FORMAT),
            tradeSuggestionTime: currentDate.format(DATE_TIME_FORMAT),
            tradeType: 'BBBBBB',
            actualPL: 1,
            slPrice: 1,
            currentMarketPrice: 1,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            tradeInTime: currentDate,
            tradeOutTime: currentDate,
            tradeDate: currentDate,
            tradeSuggestionTime: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a TradeSuggestion', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addTradeSuggestionToCollectionIfMissing', () => {
        it('should add a TradeSuggestion to an empty array', () => {
          const tradeSuggestion: ITradeSuggestion = { id: 123 };
          expectedResult = service.addTradeSuggestionToCollectionIfMissing([], tradeSuggestion);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(tradeSuggestion);
        });

        it('should not add a TradeSuggestion to an array that contains it', () => {
          const tradeSuggestion: ITradeSuggestion = { id: 123 };
          const tradeSuggestionCollection: ITradeSuggestion[] = [
            {
              ...tradeSuggestion,
            },
            { id: 456 },
          ];
          expectedResult = service.addTradeSuggestionToCollectionIfMissing(tradeSuggestionCollection, tradeSuggestion);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a TradeSuggestion to an array that doesn't contain it", () => {
          const tradeSuggestion: ITradeSuggestion = { id: 123 };
          const tradeSuggestionCollection: ITradeSuggestion[] = [{ id: 456 }];
          expectedResult = service.addTradeSuggestionToCollectionIfMissing(tradeSuggestionCollection, tradeSuggestion);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(tradeSuggestion);
        });

        it('should add only unique TradeSuggestion to an array', () => {
          const tradeSuggestionArray: ITradeSuggestion[] = [{ id: 123 }, { id: 456 }, { id: 97789 }];
          const tradeSuggestionCollection: ITradeSuggestion[] = [{ id: 123 }];
          expectedResult = service.addTradeSuggestionToCollectionIfMissing(tradeSuggestionCollection, ...tradeSuggestionArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const tradeSuggestion: ITradeSuggestion = { id: 123 };
          const tradeSuggestion2: ITradeSuggestion = { id: 456 };
          expectedResult = service.addTradeSuggestionToCollectionIfMissing([], tradeSuggestion, tradeSuggestion2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(tradeSuggestion);
          expect(expectedResult).toContain(tradeSuggestion2);
        });

        it('should accept null and undefined values', () => {
          const tradeSuggestion: ITradeSuggestion = { id: 123 };
          expectedResult = service.addTradeSuggestionToCollectionIfMissing([], null, tradeSuggestion, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(tradeSuggestion);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
