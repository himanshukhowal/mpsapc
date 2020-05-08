import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IManuscript } from 'app/shared/model/manuscript.model';
import { ManuscriptService } from './manuscript.service';

@Component({
  templateUrl: './manuscript-delete-dialog.component.html'
})
export class ManuscriptDeleteDialogComponent {
  manuscript?: IManuscript;

  constructor(
    protected manuscriptService: ManuscriptService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.manuscriptService.delete(id).subscribe(() => {
      this.eventManager.broadcast('manuscriptListModification');
      this.activeModal.close();
    });
  }
}
