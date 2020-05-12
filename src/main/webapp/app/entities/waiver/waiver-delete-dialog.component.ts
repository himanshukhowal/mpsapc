import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IWaiver } from 'app/shared/model/waiver.model';
import { WaiverService } from './waiver.service';

@Component({
  templateUrl: './waiver-delete-dialog.component.html'
})
export class WaiverDeleteDialogComponent {
  waiver?: IWaiver;

  constructor(protected waiverService: WaiverService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.waiverService.delete(id).subscribe(() => {
      this.eventManager.broadcast('waiverListModification');
      this.activeModal.close();
    });
  }
}
