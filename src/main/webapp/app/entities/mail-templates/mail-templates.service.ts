import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IMailTemplates } from 'app/shared/model/mail-templates.model';

type EntityResponseType = HttpResponse<IMailTemplates>;
type EntityArrayResponseType = HttpResponse<IMailTemplates[]>;

@Injectable({ providedIn: 'root' })
export class MailTemplatesService {
  public resourceUrl = SERVER_API_URL + 'api/mail-templates';

  constructor(protected http: HttpClient) {}

  create(mailTemplates: IMailTemplates): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(mailTemplates);
    return this.http
      .post<IMailTemplates>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(mailTemplates: IMailTemplates): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(mailTemplates);
    return this.http
      .put<IMailTemplates>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IMailTemplates>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IMailTemplates[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(mailTemplates: IMailTemplates): IMailTemplates {
    const copy: IMailTemplates = Object.assign({}, mailTemplates, {
      dateCreated:
        mailTemplates.dateCreated && mailTemplates.dateCreated.isValid() ? mailTemplates.dateCreated.format(DATE_FORMAT) : undefined,
      dateModified:
        mailTemplates.dateModified && mailTemplates.dateModified.isValid() ? mailTemplates.dateModified.format(DATE_FORMAT) : undefined
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
      res.body.forEach((mailTemplates: IMailTemplates) => {
        mailTemplates.dateCreated = mailTemplates.dateCreated ? moment(mailTemplates.dateCreated) : undefined;
        mailTemplates.dateModified = mailTemplates.dateModified ? moment(mailTemplates.dateModified) : undefined;
      });
    }
    return res;
  }
}