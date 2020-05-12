import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IContactUs } from 'app/shared/model/contact-us.model';
import { ContactUsService } from './contact-us.service';
import { ContactUsDeleteDialogComponent } from './contact-us-delete-dialog.component';

@Component({
  selector: 'jhi-contact-us',
  templateUrl: './contact-us.component.html'
})
export class ContactUsComponent implements OnInit, OnDestroy {
  contactuses?: IContactUs[];
  eventSubscriber?: Subscription;

  constructor(protected contactUsService: ContactUsService, protected eventManager: JhiEventManager, protected modalService: NgbModal) {}

  loadAll(): void {
    this.contactUsService.query().subscribe((res: HttpResponse<IContactUs[]>) => (this.contactuses = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInContactuses();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IContactUs): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInContactuses(): void {
    this.eventSubscriber = this.eventManager.subscribe('contactUsListModification', () => this.loadAll());
  }

  delete(contactUs: IContactUs): void {
    const modalRef = this.modalService.open(ContactUsDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.contactUs = contactUs;
  }
}
