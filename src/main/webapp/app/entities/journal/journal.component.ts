import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IJournal } from 'app/shared/model/journal.model';
import { JournalService } from './journal.service';
import { JournalDeleteDialogComponent } from './journal-delete-dialog.component';

@Component({
  selector: 'jhi-journal',
  templateUrl: './journal.component.html'
})
export class JournalComponent implements OnInit, OnDestroy {
  journals?: IJournal[];
  eventSubscriber?: Subscription;

  constructor(protected journalService: JournalService, protected eventManager: JhiEventManager, protected modalService: NgbModal) {}

  loadAll(): void {
    this.journalService.query().subscribe((res: HttpResponse<IJournal[]>) => (this.journals = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInJournals();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IJournal): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInJournals(): void {
    this.eventSubscriber = this.eventManager.subscribe('journalListModification', () => this.loadAll());
  }

  delete(journal: IJournal): void {
    const modalRef = this.modalService.open(JournalDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.journal = journal;
  }
}
