import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IManuscript } from 'app/shared/model/manuscript.model';

@Component({
  selector: 'jhi-manuscript-detail',
  templateUrl: './manuscript-detail.component.html'
})
export class ManuscriptDetailComponent implements OnInit {
  manuscript: IManuscript | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ manuscript }) => (this.manuscript = manuscript));
  }

  previousState(): void {
    window.history.back();
  }
}
