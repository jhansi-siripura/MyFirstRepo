import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITradeSuggestion, getTradeSuggestionIdentifier } from '../trade-suggestion.model';

export type EntityResponseType = HttpResponse<ITradeSuggestion>;
export type EntityArrayResponseType = HttpResponse<ITradeSuggestion[]>;

@Injectable({ providedIn: 'root' })
export class TradeSuggestionService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/trade-suggestions');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(tradeSuggestion: ITradeSuggestion): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(tradeSuggestion);
    return this.http
      .post<ITradeSuggestion>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(tradeSuggestion: ITradeSuggestion): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(tradeSuggestion);
    return this.http
      .put<ITradeSuggestion>(`${this.resourceUrl}/${getTradeSuggestionIdentifier(tradeSuggestion) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(tradeSuggestion: ITradeSuggestion): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(tradeSuggestion);
    return this.http
      .patch<ITradeSuggestion>(`${this.resourceUrl}/${getTradeSuggestionIdentifier(tradeSuggestion) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ITradeSuggestion>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ITradeSuggestion[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addTradeSuggestionToCollectionIfMissing(
    tradeSuggestionCollection: ITradeSuggestion[],
    ...tradeSuggestionsToCheck: (ITradeSuggestion | null | undefined)[]
  ): ITradeSuggestion[] {
    const tradeSuggestions: ITradeSuggestion[] = tradeSuggestionsToCheck.filter(isPresent);
    if (tradeSuggestions.length > 0) {
      const tradeSuggestionCollectionIdentifiers = tradeSuggestionCollection.map(
        tradeSuggestionItem => getTradeSuggestionIdentifier(tradeSuggestionItem)!
      );
      const tradeSuggestionsToAdd = tradeSuggestions.filter(tradeSuggestionItem => {
        const tradeSuggestionIdentifier = getTradeSuggestionIdentifier(tradeSuggestionItem);
        if (tradeSuggestionIdentifier == null || tradeSuggestionCollectionIdentifiers.includes(tradeSuggestionIdentifier)) {
          return false;
        }
        tradeSuggestionCollectionIdentifiers.push(tradeSuggestionIdentifier);
        return true;
      });
      return [...tradeSuggestionsToAdd, ...tradeSuggestionCollection];
    }
    return tradeSuggestionCollection;
  }

  protected convertDateFromClient(tradeSuggestion: ITradeSuggestion): ITradeSuggestion {
    return Object.assign({}, tradeSuggestion, {
      tradeInTime: tradeSuggestion.tradeInTime?.isValid() ? tradeSuggestion.tradeInTime.toJSON() : undefined,
      tradeOutTime: tradeSuggestion.tradeOutTime?.isValid() ? tradeSuggestion.tradeOutTime.toJSON() : undefined,
      tradeDate: tradeSuggestion.tradeDate?.isValid() ? tradeSuggestion.tradeDate.format(DATE_FORMAT) : undefined,
      tradeSuggestionTime: tradeSuggestion.tradeSuggestionTime?.isValid() ? tradeSuggestion.tradeSuggestionTime.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.tradeInTime = res.body.tradeInTime ? dayjs(res.body.tradeInTime) : undefined;
      res.body.tradeOutTime = res.body.tradeOutTime ? dayjs(res.body.tradeOutTime) : undefined;
      res.body.tradeDate = res.body.tradeDate ? dayjs(res.body.tradeDate) : undefined;
      res.body.tradeSuggestionTime = res.body.tradeSuggestionTime ? dayjs(res.body.tradeSuggestionTime) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((tradeSuggestion: ITradeSuggestion) => {
        tradeSuggestion.tradeInTime = tradeSuggestion.tradeInTime ? dayjs(tradeSuggestion.tradeInTime) : undefined;
        tradeSuggestion.tradeOutTime = tradeSuggestion.tradeOutTime ? dayjs(tradeSuggestion.tradeOutTime) : undefined;
        tradeSuggestion.tradeDate = tradeSuggestion.tradeDate ? dayjs(tradeSuggestion.tradeDate) : undefined;
        tradeSuggestion.tradeSuggestionTime = tradeSuggestion.tradeSuggestionTime ? dayjs(tradeSuggestion.tradeSuggestionTime) : undefined;
      });
    }
    return res;
  }
}
