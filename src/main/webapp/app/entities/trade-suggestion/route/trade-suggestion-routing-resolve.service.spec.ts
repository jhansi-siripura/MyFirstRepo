jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ITradeSuggestion, TradeSuggestion } from '../trade-suggestion.model';
import { TradeSuggestionService } from '../service/trade-suggestion.service';

import { TradeSuggestionRoutingResolveService } from './trade-suggestion-routing-resolve.service';

describe('Service Tests', () => {
  describe('TradeSuggestion routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: TradeSuggestionRoutingResolveService;
    let service: TradeSuggestionService;
    let resultTradeSuggestion: ITradeSuggestion | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(TradeSuggestionRoutingResolveService);
      service = TestBed.inject(TradeSuggestionService);
      resultTradeSuggestion = undefined;
    });

    describe('resolve', () => {
      it('should return ITradeSuggestion returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultTradeSuggestion = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultTradeSuggestion).toEqual({ id: 123 });
      });

      it('should return new ITradeSuggestion if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultTradeSuggestion = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultTradeSuggestion).toEqual(new TradeSuggestion());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultTradeSuggestion = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultTradeSuggestion).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
