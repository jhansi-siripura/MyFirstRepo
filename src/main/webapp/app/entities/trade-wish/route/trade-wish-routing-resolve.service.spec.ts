jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ITradeWish, TradeWish } from '../trade-wish.model';
import { TradeWishService } from '../service/trade-wish.service';

import { TradeWishRoutingResolveService } from './trade-wish-routing-resolve.service';

describe('Service Tests', () => {
  describe('TradeWish routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: TradeWishRoutingResolveService;
    let service: TradeWishService;
    let resultTradeWish: ITradeWish | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(TradeWishRoutingResolveService);
      service = TestBed.inject(TradeWishService);
      resultTradeWish = undefined;
    });

    describe('resolve', () => {
      it('should return ITradeWish returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultTradeWish = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultTradeWish).toEqual({ id: 123 });
      });

      it('should return new ITradeWish if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultTradeWish = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultTradeWish).toEqual(new TradeWish());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultTradeWish = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultTradeWish).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
