import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IGratitude, getGratitudeIdentifier } from '../gratitude.model';

export type EntityResponseType = HttpResponse<IGratitude>;
export type EntityArrayResponseType = HttpResponse<IGratitude[]>;

@Injectable({ providedIn: 'root' })
export class GratitudeService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/gratitudes');
  public resourceUrl_Today = this.applicationConfigService.getEndpointFor('api/gratitudes/new');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(gratitude: IGratitude): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(gratitude);
    return this.http
      .post<IGratitude>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(gratitude: IGratitude): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(gratitude);
    return this.http
      .put<IGratitude>(`${this.resourceUrl}/${getGratitudeIdentifier(gratitude) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(gratitude: IGratitude): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(gratitude);
    return this.http
      .patch<IGratitude>(`${this.resourceUrl}/${getGratitudeIdentifier(gratitude) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IGratitude>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IGratitude[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  getTodaysGratitude(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IGratitude[]>(this.resourceUrl_Today, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addGratitudeToCollectionIfMissing(
    gratitudeCollection: IGratitude[],
    ...gratitudesToCheck: (IGratitude | null | undefined)[]
  ): IGratitude[] {
    const gratitudes: IGratitude[] = gratitudesToCheck.filter(isPresent);
    if (gratitudes.length > 0) {
      const gratitudeCollectionIdentifiers = gratitudeCollection.map(gratitudeItem => getGratitudeIdentifier(gratitudeItem)!);
      const gratitudesToAdd = gratitudes.filter(gratitudeItem => {
        const gratitudeIdentifier = getGratitudeIdentifier(gratitudeItem);
        if (gratitudeIdentifier == null || gratitudeCollectionIdentifiers.includes(gratitudeIdentifier)) {
          return false;
        }
        gratitudeCollectionIdentifiers.push(gratitudeIdentifier);
        return true;
      });
      return [...gratitudesToAdd, ...gratitudeCollection];
    }
    return gratitudeCollection;
  }

  protected convertDateFromClient(gratitude: IGratitude): IGratitude {
    return Object.assign({}, gratitude, {
      createdDate: gratitude.createdDate?.isValid() ? gratitude.createdDate.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.createdDate = res.body.createdDate ? dayjs(res.body.createdDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((gratitude: IGratitude) => {
        gratitude.createdDate = gratitude.createdDate ? dayjs(gratitude.createdDate) : undefined;
      });
    }
    return res;
  }
}
