import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IDiscount } from 'app/shared/model/discount.model';

type EntityResponseType = HttpResponse<IDiscount>;
type EntityArrayResponseType = HttpResponse<IDiscount[]>;

@Injectable({ providedIn: 'root' })
export class DiscountService {
  public resourceUrl = SERVER_API_URL + 'api/discounts';

  constructor(protected http: HttpClient) {}

  create(discount: IDiscount): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(discount);
    return this.http
      .post<IDiscount>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(discount: IDiscount): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(discount);
    return this.http
      .put<IDiscount>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IDiscount>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IDiscount[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(discount: IDiscount): IDiscount {
    const copy: IDiscount = Object.assign({}, discount, {
      dateCreated: discount.dateCreated && discount.dateCreated.isValid() ? discount.dateCreated.format(DATE_FORMAT) : undefined,
      dateModified: discount.dateModified && discount.dateModified.isValid() ? discount.dateModified.format(DATE_FORMAT) : undefined
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
      res.body.forEach((discount: IDiscount) => {
        discount.dateCreated = discount.dateCreated ? moment(discount.dateCreated) : undefined;
        discount.dateModified = discount.dateModified ? moment(discount.dateModified) : undefined;
      });
    }
    return res;
  }
}
