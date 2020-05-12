import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IManuscript } from 'app/shared/model/manuscript.model';

type EntityResponseType = HttpResponse<IManuscript>;
type EntityArrayResponseType = HttpResponse<IManuscript[]>;

@Injectable({ providedIn: 'root' })
export class ManuscriptService {
  public resourceUrl = SERVER_API_URL + 'api/manuscripts';

  constructor(protected http: HttpClient) {}

  create(manuscript: IManuscript): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(manuscript);
    return this.http
      .post<IManuscript>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(manuscript: IManuscript): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(manuscript);
    return this.http
      .put<IManuscript>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IManuscript>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IManuscript[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(manuscript: IManuscript): IManuscript {
    const copy: IManuscript = Object.assign({}, manuscript, {
      dateCreated: manuscript.dateCreated && manuscript.dateCreated.isValid() ? manuscript.dateCreated.format(DATE_FORMAT) : undefined,
      dateModified: manuscript.dateModified && manuscript.dateModified.isValid() ? manuscript.dateModified.format(DATE_FORMAT) : undefined
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
      res.body.forEach((manuscript: IManuscript) => {
        manuscript.dateCreated = manuscript.dateCreated ? moment(manuscript.dateCreated) : undefined;
        manuscript.dateModified = manuscript.dateModified ? moment(manuscript.dateModified) : undefined;
      });
    }
    return res;
  }
}
