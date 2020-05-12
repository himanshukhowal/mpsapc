import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IManuscript } from 'app/shared/model/manuscript.model';
import { ManuscriptService } from './manuscript.service';
import { ManuscriptDeleteDialogComponent } from './manuscript-delete-dialog.component';

@Component({
  selector: 'jhi-manuscript',
  templateUrl: './manuscript.component.html'
})
export class ManuscriptComponent implements OnInit, OnDestroy {
  manuscripts?: IManuscript[];
  eventSubscriber?: Subscription;

  constructor(protected manuscriptService: ManuscriptService, protected eventManager: JhiEventManager, protected modalService: NgbModal) {}

  loadAll(): void {
    this.manuscriptService.query().subscribe((res: HttpResponse<IManuscript[]>) => (this.manuscripts = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInManuscripts();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IManuscript): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInManuscripts(): void {
    this.eventSubscriber = this.eventManager.subscribe('manuscriptListModification', () => this.loadAll());
  }

  delete(manuscript: IManuscript): void {
    const modalRef = this.modalService.open(ManuscriptDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.manuscript = manuscript;
  }
}
