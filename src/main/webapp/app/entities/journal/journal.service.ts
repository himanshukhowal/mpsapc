import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IJournal } from 'app/shared/model/journal.model';

type EntityResponseType = HttpResponse<IJournal>;
type EntityArrayResponseType = HttpResponse<IJournal[]>;

@Injectable({ providedIn: 'root' })
export class JournalService {
  public resourceUrl = SERVER_API_URL + 'api/journals';

  constructor(protected http: HttpClient) {}

  create(journal: IJournal): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(journal);
    return this.http
      .post<IJournal>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(journal: IJournal): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(journal);
    return this.http
      .put<IJournal>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IJournal>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IJournal[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(journal: IJournal): IJournal {
    const copy: IJournal = Object.assign({}, journal, {
      dateCreated: journal.dateCreated && journal.dateCreated.isValid() ? journal.dateCreated.format(DATE_FORMAT) : undefined,
      dateModified: journal.dateModified && journal.dateModified.isValid() ? journal.dateModified.format(DATE_FORMAT) : undefined
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
      res.body.forEach((journal: IJournal) => {
        journal.dateCreated = journal.dateCreated ? moment(journal.dateCreated) : undefined;
        journal.dateModified = journal.dateModified ? moment(journal.dateModified) : undefined;
      });
    }
    return res;
  }
}
