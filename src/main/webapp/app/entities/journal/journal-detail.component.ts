import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IJournal } from 'app/shared/model/journal.model';

@Component({
  selector: 'jhi-journal-detail',
  templateUrl: './journal-detail.component.html'
})
export class JournalDetailComponent implements OnInit {
  journal: IJournal | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ journal }) => (this.journal = journal));
  }

  previousState(): void {
    window.history.back();
  }
}
