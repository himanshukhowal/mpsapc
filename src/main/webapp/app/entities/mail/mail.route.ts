import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IMail, Mail } from 'app/shared/model/mail.model';
import { MailService } from './mail.service';
import { MailComponent } from './mail.component';
import { MailDetailComponent } from './mail-detail.component';
import { MailUpdateComponent } from './mail-update.component';

@Injectable({ providedIn: 'root' })
export class MailResolve implements Resolve<IMail> {
  constructor(private service: MailService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IMail> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((mail: HttpResponse<Mail>) => {
          if (mail.body) {
            return of(mail.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Mail());
  }
}

export const mailRoute: Routes = [
  {
    path: '',
    component: MailComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'mpsapcApp.mail.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: MailDetailComponent,
    resolve: {
      mail: MailResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'mpsapcApp.mail.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: MailUpdateComponent,
    resolve: {
      mail: MailResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'mpsapcApp.mail.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: MailUpdateComponent,
    resolve: {
      mail: MailResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'mpsapcApp.mail.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
