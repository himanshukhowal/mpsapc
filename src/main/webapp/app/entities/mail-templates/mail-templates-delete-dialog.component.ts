import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IMailTemplates } from 'app/shared/model/mail-templates.model';
import { MailTemplatesService } from './mail-templates.service';

@Component({
  templateUrl: './mail-templates-delete-dialog.component.html'
})
export class MailTemplatesDeleteDialogComponent {
  mailTemplates?: IMailTemplates;

  constructor(
    protected mailTemplatesService: MailTemplatesService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.mailTemplatesService.delete(id).subscribe(() => {
      this.eventManager.broadcast('mailTemplatesListModification');
      this.activeModal.close();
    });
  }
}
