import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IMail } from 'app/shared/model/mail.model';

type EntityResponseType = HttpResponse<IMail>;
type EntityArrayResponseType = HttpResponse<IMail[]>;

@Injectable({ providedIn: 'root' })
export class MailService {
  public resourceUrl = SERVER_API_URL + 'api/mail';

  constructor(protected http: HttpClient) {}

  create(mail: IMail): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(mail);
    return this.http
      .post<IMail>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(mail: IMail): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(mail);
    return this.http
      .put<IMail>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IMail>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IMail[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(mail: IMail): IMail {
    const copy: IMail = Object.assign({}, mail, {
      dateCreated: mail.dateCreated && mail.dateCreated.isValid() ? mail.dateCreated.format(DATE_FORMAT) : undefined,
      dateModified: mail.dateModified && mail.dateModified.isValid() ? mail.dateModified.format(DATE_FORMAT) : undefined
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
      res.body.forEach((mail: IMail) => {
        mail.dateCreated = mail.dateCreated ? moment(mail.dateCreated) : undefined;
        mail.dateModified = mail.dateModified ? moment(mail.dateModified) : undefined;
      });
    }
    return res;
  }
}
