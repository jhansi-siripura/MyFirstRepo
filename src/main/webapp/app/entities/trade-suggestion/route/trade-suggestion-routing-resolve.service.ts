import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITradeSuggestion, TradeSuggestion } from '../trade-suggestion.model';
import { TradeSuggestionService } from '../service/trade-suggestion.service';

@Injectable({ providedIn: 'root' })
export class TradeSuggestionRoutingResolveService implements Resolve<ITradeSuggestion> {
  constructor(protected service: TradeSuggestionService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITradeSuggestion> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((tradeSuggestion: HttpResponse<TradeSuggestion>) => {
          if (tradeSuggestion.body) {
            return of(tradeSuggestion.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new TradeSuggestion());
  }
}
