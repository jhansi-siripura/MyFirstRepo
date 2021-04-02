import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IWish, getWishIdentifier } from '../wish.model';

export type EntityResponseType = HttpResponse<IWish>;
export type EntityArrayResponseType = HttpResponse<IWish[]>;

@Injectable({ providedIn: 'root' })
export class WishService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/wishes');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(wish: IWish): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(wish);
    return this.http
      .post<IWish>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(wish: IWish): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(wish);
    return this.http
      .put<IWish>(`${this.resourceUrl}/${getWishIdentifier(wish) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(wish: IWish): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(wish);
    return this.http
      .patch<IWish>(`${this.resourceUrl}/${getWishIdentifier(wish) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IWish>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IWish[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addWishToCollectionIfMissing(wishCollection: IWish[], ...wishesToCheck: (IWish | null | undefined)[]): IWish[] {
    const wishes: IWish[] = wishesToCheck.filter(isPresent);
    if (wishes.length > 0) {
      const wishCollectionIdentifiers = wishCollection.map(wishItem => getWishIdentifier(wishItem)!);
      const wishesToAdd = wishes.filter(wishItem => {
        const wishIdentifier = getWishIdentifier(wishItem);
        if (wishIdentifier == null || wishCollectionIdentifiers.includes(wishIdentifier)) {
          return false;
        }
        wishCollectionIdentifiers.push(wishIdentifier);
        return true;
      });
      return [...wishesToAdd, ...wishCollection];
    }
    return wishCollection;
  }

  protected convertDateFromClient(wish: IWish): IWish {
    return Object.assign({}, wish, {
      startDate: wish.startDate?.isValid() ? wish.startDate.format(DATE_FORMAT) : undefined,
      fulfilledDate: wish.fulfilledDate?.isValid() ? wish.fulfilledDate.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.startDate = res.body.startDate ? dayjs(res.body.startDate) : undefined;
      res.body.fulfilledDate = res.body.fulfilledDate ? dayjs(res.body.fulfilledDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((wish: IWish) => {
        wish.startDate = wish.startDate ? dayjs(wish.startDate) : undefined;
        wish.fulfilledDate = wish.fulfilledDate ? dayjs(wish.fulfilledDate) : undefined;
      });
    }
    return res;
  }
}
