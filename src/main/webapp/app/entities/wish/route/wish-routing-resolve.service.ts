import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IWish, Wish } from '../wish.model';
import { WishService } from '../service/wish.service';

@Injectable({ providedIn: 'root' })
export class WishRoutingResolveService implements Resolve<IWish> {
  constructor(protected service: WishService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IWish> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((wish: HttpResponse<Wish>) => {
          if (wish.body) {
            return of(wish.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Wish());
  }
}
