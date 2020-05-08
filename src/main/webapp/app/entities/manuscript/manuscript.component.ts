import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IManuscript } from 'app/shared/model/manuscript.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { ManuscriptService } from './manuscript.service';
import { ManuscriptDeleteDialogComponent } from './manuscript-delete-dialog.component';

@Component({
  selector: 'jhi-manuscript',
  templateUrl: './manuscript.component.html'
})
export class ManuscriptComponent implements OnInit, OnDestroy {
  manuscripts?: IManuscript[];
  eventSubscriber?: Subscription;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page!: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;

  constructor(
    protected manuscriptService: ManuscriptService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal
  ) {}

  loadPage(page?: number): void {
    const pageToLoad: number = page || this.page;

    this.manuscriptService
      .query({
        page: pageToLoad - 1,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe(
        (res: HttpResponse<IManuscript[]>) => this.onSuccess(res.body, res.headers, pageToLoad),
        () => this.onError()
      );
  }

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(data => {
      this.page = data.pagingParams.page;
      this.ascending = data.pagingParams.ascending;
      this.predicate = data.pagingParams.predicate;
      this.ngbPaginationPage = data.pagingParams.page;
      this.loadPage();
    });
    this.registerChangeInManuscripts();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IManuscript): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInManuscripts(): void {
    this.eventSubscriber = this.eventManager.subscribe('manuscriptListModification', () => this.loadPage());
  }

  delete(manuscript: IManuscript): void {
    const modalRef = this.modalService.open(ManuscriptDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.manuscript = manuscript;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected onSuccess(data: IManuscript[] | null, headers: HttpHeaders, page: number): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    this.router.navigate(['/manuscript'], {
      queryParams: {
        page: this.page,
        size: this.itemsPerPage,
        sort: this.predicate + ',' + (this.ascending ? 'asc' : 'desc')
      }
    });
    this.manuscripts = data || [];
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page;
  }
}
