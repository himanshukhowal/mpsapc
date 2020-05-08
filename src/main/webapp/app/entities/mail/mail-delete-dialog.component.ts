import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IMail } from 'app/shared/model/mail.model';
import { MailService } from './mail.service';

@Component({
  templateUrl: './mail-delete-dialog.component.html'
})
export class MailDeleteDialogComponent {
  mail?: IMail;

  constructor(protected mailService: MailService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.mailService.delete(id).subscribe(() => {
      this.eventManager.broadcast('mailListModification');
      this.activeModal.close();
    });
  }
}
