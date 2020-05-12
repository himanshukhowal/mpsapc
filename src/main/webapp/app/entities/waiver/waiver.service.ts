import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IWaiver } from 'app/shared/model/waiver.model';

type EntityResponseType = HttpResponse<IWaiver>;
type EntityArrayResponseType = HttpResponse<IWaiver[]>;

@Injectable({ providedIn: 'root' })
export class WaiverService {
  public resourceUrl = SERVER_API_URL + 'api/waivers';

  constructor(protected http: HttpClient) {}

  create(waiver: IWaiver): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(waiver);
    return this.http
      .post<IWaiver>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(waiver: IWaiver): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(waiver);
    return this.http
      .put<IWaiver>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IWaiver>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IWaiver[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(waiver: IWaiver): IWaiver {
    const copy: IWaiver = Object.assign({}, waiver, {
      dateCreated: waiver.dateCreated && waiver.dateCreated.isValid() ? waiver.dateCreated.format(DATE_FORMAT) : undefined,
      dateModified: waiver.dateModified && waiver.dateModified.isValid() ? waiver.dateModified.format(DATE_FORMAT) : undefined
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dateCreated = res.body.dateCreated ? moment(res.body.dateCreated) : undefined;
      res.body.dateModified = res.body.dateModified ? moment(res.body.dateModified) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((waiver: IWaiver) => {
        waiver.dateCreated = waiver.dateCreated ? moment(waiver.dateCreated) : undefined;
        waiver.dateModified = waiver.dateModified ? moment(waiver.dateModified) : undefined;
      });
    }
    return res;
  }
}
