import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IMailTemplates } from 'app/shared/model/mail-templates.model';
import { MailTemplatesService } from './mail-templates.service';
import { MailTemplatesDeleteDialogComponent } from './mail-templates-delete-dialog.component';

@Component({
  selector: 'jhi-mail-templates',
  templateUrl: './mail-templates.component.html'
})
export class MailTemplatesComponent implements OnInit, OnDestroy {
  mailTemplates?: IMailTemplates[];
  eventSubscriber?: Subscription;

  constructor(
    protected mailTemplatesService: MailTemplatesService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal
  ) {}

  loadAll(): void {
    this.mailTemplatesService.query().subscribe((res: HttpResponse<IMailTemplates[]>) => (this.mailTemplates = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
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
    this.eventSubscriber = this.eventManager.subscribe('mailTemplatesListModification', () => this.loadAll());
  }

  delete(mailTemplates: IMailTemplates): void {
    const modalRef = this.modalService.open(MailTemplatesDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.mailTemplates = mailTemplates;
  }
}
