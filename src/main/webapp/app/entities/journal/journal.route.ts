import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IJournal, Journal } from 'app/shared/model/journal.model';
import { JournalService } from './journal.service';
import { JournalComponent } from './journal.component';
import { JournalDetailComponent } from './journal-detail.component';
import { JournalUpdateComponent } from './journal-update.component';

@Injectable({ providedIn: 'root' })
export class JournalResolve implements Resolve<IJournal> {
  constructor(private service: JournalService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IJournal> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((journal: HttpResponse<Journal>) => {
          if (journal.body) {
            return of(journal.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Journal());
  }
}

export const journalRoute: Routes = [
  {
    path: '',
    component: JournalComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'mpsapcApp.journal.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: JournalDetailComponent,
    resolve: {
      journal: JournalResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'mpsapcApp.journal.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: JournalUpdateComponent,
    resolve: {
      journal: JournalResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'mpsapcApp.journal.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: JournalUpdateComponent,
    resolve: {
      journal: JournalResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'mpsapcApp.journal.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
