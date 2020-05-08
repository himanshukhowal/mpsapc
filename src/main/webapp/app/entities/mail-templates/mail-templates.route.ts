import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IMailTemplates, MailTemplates } from 'app/shared/model/mail-templates.model';
import { MailTemplatesService } from './mail-templates.service';
import { MailTemplatesComponent } from './mail-templates.component';
import { MailTemplatesDetailComponent } from './mail-templates-detail.component';
import { MailTemplatesUpdateComponent } from './mail-templates-update.component';

@Injectable({ providedIn: 'root' })
export class MailTemplatesResolve implements Resolve<IMailTemplates> {
  constructor(private service: MailTemplatesService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IMailTemplates> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((mailTemplates: HttpResponse<MailTemplates>) => {
          if (mailTemplates.body) {
            return of(mailTemplates.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new MailTemplates());
  }
}

export const mailTemplatesRoute: Routes = [
  {
    path: '',
    component: MailTemplatesComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'mpsapcApp.mailTemplates.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: MailTemplatesDetailComponent,
    resolve: {
      mailTemplates: MailTemplatesResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'mpsapcApp.mailTemplates.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: MailTemplatesUpdateComponent,
    resolve: {
      mailTemplates: MailTemplatesResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'mpsapcApp.mailTemplates.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: MailTemplatesUpdateComponent,
    resolve: {
      mailTemplates: MailTemplatesResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'mpsapcApp.mailTemplates.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
