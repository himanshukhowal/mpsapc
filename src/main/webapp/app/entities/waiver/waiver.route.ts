import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IWaiver, Waiver } from 'app/shared/model/waiver.model';
import { WaiverService } from './waiver.service';
import { WaiverComponent } from './waiver.component';
import { WaiverDetailComponent } from './waiver-detail.component';
import { WaiverUpdateComponent } from './waiver-update.component';

@Injectable({ providedIn: 'root' })
export class WaiverResolve implements Resolve<IWaiver> {
  constructor(private service: WaiverService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IWaiver> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((waiver: HttpResponse<Waiver>) => {
          if (waiver.body) {
            return of(waiver.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Waiver());
  }
}

export const waiverRoute: Routes = [
  {
    path: '',
    component: WaiverComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'mpsapcApp.waiver.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: WaiverDetailComponent,
    resolve: {
      waiver: WaiverResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'mpsapcApp.waiver.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: WaiverUpdateComponent,
    resolve: {
      waiver: WaiverResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'mpsapcApp.waiver.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: WaiverUpdateComponent,
    resolve: {
      waiver: WaiverResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'mpsapcApp.waiver.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
