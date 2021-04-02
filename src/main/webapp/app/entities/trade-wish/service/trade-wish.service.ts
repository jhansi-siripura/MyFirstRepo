import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITradeWish, getTradeWishIdentifier } from '../trade-wish.model';

export type EntityResponseType = HttpResponse<ITradeWish>;
export type EntityArrayResponseType = HttpResponse<ITradeWish[]>;

@Injectable({ providedIn: 'root' })
export class TradeWishService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/trade-wishes');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(tradeWish: ITradeWish): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(tradeWish);
    return this.http
      .post<ITradeWish>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(tradeWish: ITradeWish): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(tradeWish);
    return this.http
      .put<ITradeWish>(`${this.resourceUrl}/${getTradeWishIdentifier(tradeWish) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(tradeWish: ITradeWish): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(tradeWish);
    return this.http
      .patch<ITradeWish>(`${this.resourceUrl}/${getTradeWishIdentifier(tradeWish) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ITradeWish>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ITradeWish[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addTradeWishToCollectionIfMissing(
    tradeWishCollection: ITradeWish[],
    ...tradeWishesToCheck: (ITradeWish | null | undefined)[]
  ): ITradeWish[] {
    const tradeWishes: ITradeWish[] = tradeWishesToCheck.filter(isPresent);
    if (tradeWishes.length > 0) {
      const tradeWishCollectionIdentifiers = tradeWishCollection.map(tradeWishItem => getTradeWishIdentifier(tradeWishItem)!);
      const tradeWishesToAdd = tradeWishes.filter(tradeWishItem => {
        const tradeWishIdentifier = getTradeWishIdentifier(tradeWishItem);
        if (tradeWishIdentifier == null || tradeWishCollectionIdentifiers.includes(tradeWishIdentifier)) {
          return false;
        }
        tradeWishCollectionIdentifiers.push(tradeWishIdentifier);
        return true;
      });
      return [...tradeWishesToAdd, ...tradeWishCollection];
    }
    return tradeWishCollection;
  }

  protected convertDateFromClient(tradeWish: ITradeWish): ITradeWish {
    return Object.assign({}, tradeWish, {
      pickedDate: tradeWish.pickedDate?.isValid() ? tradeWish.pickedDate.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.pickedDate = res.body.pickedDate ? dayjs(res.body.pickedDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((tradeWish: ITradeWish) => {
        tradeWish.pickedDate = tradeWish.pickedDate ? dayjs(tradeWish.pickedDate) : undefined;
      });
    }
    return res;
  }
}
