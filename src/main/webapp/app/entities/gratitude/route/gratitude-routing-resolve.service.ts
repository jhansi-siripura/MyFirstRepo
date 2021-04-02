import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IGratitude, Gratitude } from '../gratitude.model';
import { GratitudeService } from '../service/gratitude.service';

@Injectable({ providedIn: 'root' })
export class GratitudeRoutingResolveService implements Resolve<IGratitude> {
  constructor(protected service: GratitudeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IGratitude> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((gratitude: HttpResponse<Gratitude>) => {
          if (gratitude.body) {
            return of(gratitude.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Gratitude());
  }
}
