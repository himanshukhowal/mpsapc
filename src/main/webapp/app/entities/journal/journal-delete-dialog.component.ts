import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IJournal } from 'app/shared/model/journal.model';
import { JournalService } from './journal.service';

@Component({
  templateUrl: './journal-delete-dialog.component.html'
})
export class JournalDeleteDialogComponent {
  journal?: IJournal;

  constructor(protected journalService: JournalService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.journalService.delete(id).subscribe(() => {
      this.eventManager.broadcast('journalListModification');
      this.activeModal.close();
    });
  }
}
