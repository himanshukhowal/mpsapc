import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IContactUs } from 'app/shared/model/contact-us.model';

type EntityResponseType = HttpResponse<IContactUs>;
type EntityArrayResponseType = HttpResponse<IContactUs[]>;

@Injectable({ providedIn: 'root' })
export class ContactUsService {
  public resourceUrl = SERVER_API_URL + 'api/contactuses';

  constructor(protected http: HttpClient) {}

  create(contactUs: IContactUs): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(contactUs);
    return this.http
      .post<IContactUs>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(contactUs: IContactUs): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(contactUs);
    return this.http
      .put<IContactUs>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IContactUs>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IContactUs[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(contactUs: IContactUs): IContactUs {
    const copy: IContactUs = Object.assign({}, contactUs, {
      dateAdded: contactUs.dateAdded && contactUs.dateAdded.isValid() ? contactUs.dateAdded.format(DATE_FORMAT) : undefined,
      dateModified: contactUs.dateModified && contactUs.dateModified.isValid() ? contactUs.dateModified.format(DATE_FORMAT) : undefined
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dateAdded = res.body.dateAdded ? moment(res.body.dateAdded) : undefined;
      res.body.dateModified = res.body.dateModified ? moment(res.body.dateModified) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((contactUs: IContactUs) => {
        contactUs.dateAdded = contactUs.dateAdded ? moment(contactUs.dateAdded) : undefined;
        contactUs.dateModified = contactUs.dateModified ? moment(contactUs.dateModified) : undefined;
      });
    }
    return res;
  }
}
