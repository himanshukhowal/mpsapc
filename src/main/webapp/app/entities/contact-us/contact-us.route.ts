import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IContactUs, ContactUs } from 'app/shared/model/contact-us.model';
import { ContactUsService } from './contact-us.service';
import { ContactUsComponent } from './contact-us.component';
import { ContactUsDetailComponent } from './contact-us-detail.component';
import { ContactUsUpdateComponent } from './contact-us-update.component';

@Injectable({ providedIn: 'root' })
export class ContactUsResolve implements Resolve<IContactUs> {
  constructor(private service: ContactUsService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IContactUs> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((contactUs: HttpResponse<ContactUs>) => {
          if (contactUs.body) {
            return of(contactUs.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ContactUs());
  }
}

export const contactUsRoute: Routes = [
  {
    path: '',
    component: ContactUsComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'mpsapcApp.contactUs.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: ContactUsDetailComponent,
    resolve: {
      contactUs: ContactUsResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'mpsapcApp.contactUs.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: ContactUsUpdateComponent,
    resolve: {
      contactUs: ContactUsResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'mpsapcApp.contactUs.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: ContactUsUpdateComponent,
    resolve: {
      contactUs: ContactUsResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'mpsapcApp.contactUs.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
