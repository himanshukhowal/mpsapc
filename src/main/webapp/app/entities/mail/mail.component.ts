import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IMail } from 'app/shared/model/mail.model';
import { MailService } from './mail.service';
import { MailDeleteDialogComponent } from './mail-delete-dialog.component';

@Component({
  selector: 'jhi-mail',
  templateUrl: './mail.component.html'
})
export class MailComponent implements OnInit, OnDestroy {
  mail?: IMail[];
  eventSubscriber?: Subscription;

  constructor(protected mailService: MailService, protected eventManager: JhiEventManager, protected modalService: NgbModal) {}

  loadAll(): void {
    this.mailService.query().subscribe((res: HttpResponse<IMail[]>) => (this.mail = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInMail();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IMail): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInMail(): void {
    this.eventSubscriber = this.eventManager.subscribe('mailListModification', () => this.loadAll());
  }

  delete(mail: IMail): void {
    const modalRef = this.modalService.open(MailDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.mail = mail;
  }
}
