import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IMailTemplates } from 'app/shared/model/mail-templates.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { MailTemplatesService } from './mail-templates.service';
import { MailTemplatesDeleteDialogComponent } from './mail-templates-delete-dialog.component';

@Component({
  selector: 'jhi-mail-templates',
  templateUrl: './mail-templates.component.html'
})
export class MailTemplatesComponent implements OnInit, OnDestroy {
  mailTemplates?: IMailTemplates[];
  eventSubscriber?: Subscription;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page!: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;

  constructor(
    protected mailTemplatesService: MailTemplatesService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal
  ) {}

  loadPage(page?: number): void {
    const pageToLoad: number = page || this.page;

    this.mailTemplatesService
      .query({
        page: pageToLoad - 1,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe(
        (res: HttpResponse<IMailTemplates[]>) => this.onSuccess(res.body, res.headers, pageToLoad),
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
    this.registerChangeInMailTemplates();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IMailTemplates): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInMailTemplates(): void {
    this.eventSubscriber = this.eventManager.subscribe('mailTemplatesListModification', () => this.loadPage());
  }

  delete(mailTemplates: IMailTemplates): void {
    const modalRef = this.modalService.open(MailTemplatesDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.mailTemplates = mailTemplates;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected onSuccess(data: IMailTemplates[] | null, headers: HttpHeaders, page: number): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    this.router.navigate(['/mail-templates'], {
      queryParams: {
        page: this.page,
        size: this.itemsPerPage,
        sort: this.predicate + ',' + (this.ascending ? 'asc' : 'desc')
      }
    });
    this.mailTemplates = data || [];
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page;
  }
}
