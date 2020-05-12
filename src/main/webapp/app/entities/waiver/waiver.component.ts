import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IWaiver } from 'app/shared/model/waiver.model';
import { WaiverService } from './waiver.service';
import { WaiverDeleteDialogComponent } from './waiver-delete-dialog.component';

@Component({
  selector: 'jhi-waiver',
  templateUrl: './waiver.component.html'
})
export class WaiverComponent implements OnInit, OnDestroy {
  waivers?: IWaiver[];
  eventSubscriber?: Subscription;

  constructor(protected waiverService: WaiverService, protected eventManager: JhiEventManager, protected modalService: NgbModal) {}

  loadAll(): void {
    this.waiverService.query().subscribe((res: HttpResponse<IWaiver[]>) => (this.waivers = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInWaivers();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IWaiver): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInWaivers(): void {
    this.eventSubscriber = this.eventManager.subscribe('waiverListModification', () => this.loadAll());
  }

  delete(waiver: IWaiver): void {
    const modalRef = this.modalService.open(WaiverDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.waiver = waiver;
  }
}
