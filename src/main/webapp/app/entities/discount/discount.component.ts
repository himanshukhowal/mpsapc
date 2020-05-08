import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IDiscount } from 'app/shared/model/discount.model';
import { DiscountService } from './discount.service';
import { DiscountDeleteDialogComponent } from './discount-delete-dialog.component';

@Component({
  selector: 'jhi-discount',
  templateUrl: './discount.component.html'
})
export class DiscountComponent implements OnInit, OnDestroy {
  discounts?: IDiscount[];
  eventSubscriber?: Subscription;

  constructor(protected discountService: DiscountService, protected eventManager: JhiEventManager, protected modalService: NgbModal) {}

  loadAll(): void {
    this.discountService.query().subscribe((res: HttpResponse<IDiscount[]>) => (this.discounts = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInDiscounts();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IDiscount): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInDiscounts(): void {
    this.eventSubscriber = this.eventManager.subscribe('discountListModification', () => this.loadAll());
  }

  delete(discount: IDiscount): void {
    const modalRef = this.modalService.open(DiscountDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.discount = discount;
  }
}
