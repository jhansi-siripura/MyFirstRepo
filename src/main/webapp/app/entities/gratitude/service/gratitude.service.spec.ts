import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IGratitude, Gratitude } from '../gratitude.model';

import { GratitudeService } from './gratitude.service';

describe('Service Tests', () => {
  describe('Gratitude Service', () => {
    let service: GratitudeService;
    let httpMock: HttpTestingController;
    let elemDefault: IGratitude;
    let expectedResult: IGratitude | IGratitude[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(GratitudeService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        gratefulNote: 'AAAAAAA',
        createdDate: currentDate,
        loved: false,
        achieved: false,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            createdDate: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Gratitude', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            createdDate: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            createdDate: currentDate,
          },
          returnedFromService
        );

        service.create(new Gratitude()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Gratitude', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            gratefulNote: 'BBBBBB',
            createdDate: currentDate.format(DATE_TIME_FORMAT),
            loved: true,
            achieved: true,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            createdDate: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Gratitude', () => {
        const patchObject = Object.assign(
          {
            achieved: true,
          },
          new Gratitude()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            createdDate: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Gratitude', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            gratefulNote: 'BBBBBB',
            createdDate: currentDate.format(DATE_TIME_FORMAT),
            loved: true,
            achieved: true,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            createdDate: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Gratitude', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addGratitudeToCollectionIfMissing', () => {
        it('should add a Gratitude to an empty array', () => {
          const gratitude: IGratitude = { id: 123 };
          expectedResult = service.addGratitudeToCollectionIfMissing([], gratitude);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(gratitude);
        });

        it('should not add a Gratitude to an array that contains it', () => {
          const gratitude: IGratitude = { id: 123 };
          const gratitudeCollection: IGratitude[] = [
            {
              ...gratitude,
            },
            { id: 456 },
          ];
          expectedResult = service.addGratitudeToCollectionIfMissing(gratitudeCollection, gratitude);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Gratitude to an array that doesn't contain it", () => {
          const gratitude: IGratitude = { id: 123 };
          const gratitudeCollection: IGratitude[] = [{ id: 456 }];
          expectedResult = service.addGratitudeToCollectionIfMissing(gratitudeCollection, gratitude);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(gratitude);
        });

        it('should add only unique Gratitude to an array', () => {
          const gratitudeArray: IGratitude[] = [{ id: 123 }, { id: 456 }, { id: 13142 }];
          const gratitudeCollection: IGratitude[] = [{ id: 123 }];
          expectedResult = service.addGratitudeToCollectionIfMissing(gratitudeCollection, ...gratitudeArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const gratitude: IGratitude = { id: 123 };
          const gratitude2: IGratitude = { id: 456 };
          expectedResult = service.addGratitudeToCollectionIfMissing([], gratitude, gratitude2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(gratitude);
          expect(expectedResult).toContain(gratitude2);
        });

        it('should accept null and undefined values', () => {
          const gratitude: IGratitude = { id: 123 };
          expectedResult = service.addGratitudeToCollectionIfMissing([], null, gratitude, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(gratitude);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
