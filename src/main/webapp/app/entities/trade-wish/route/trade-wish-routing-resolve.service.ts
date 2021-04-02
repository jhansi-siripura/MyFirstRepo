import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITradeWish, TradeWish } from '../trade-wish.model';
import { TradeWishService } from '../service/trade-wish.service';

@Injectable({ providedIn: 'root' })
export class TradeWishRoutingResolveService implements Resolve<ITradeWish> {
  constructor(protected service: TradeWishService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITradeWish> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((tradeWish: HttpResponse<TradeWish>) => {
          if (tradeWish.body) {
            return of(tradeWish.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new TradeWish());
  }
}
