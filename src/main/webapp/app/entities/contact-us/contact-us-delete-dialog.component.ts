import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IContactUs } from 'app/shared/model/contact-us.model';
import { ContactUsService } from './contact-us.service';

@Component({
  templateUrl: './contact-us-delete-dialog.component.html'
})
export class ContactUsDeleteDialogComponent {
  contactUs?: IContactUs;

  constructor(protected contactUsService: ContactUsService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.contactUsService.delete(id).subscribe(() => {
      this.eventManager.broadcast('contactUsListModification');
      this.activeModal.close();
    });
  }
}
