import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IWaiver } from 'app/shared/model/waiver.model';

@Component({
  selector: 'jhi-waiver-detail',
  templateUrl: './waiver-detail.component.html'
})
export class WaiverDetailComponent implements OnInit {
  waiver: IWaiver | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ waiver }) => (this.waiver = waiver));
  }

  previousState(): void {
    window.history.back();
  }
}
