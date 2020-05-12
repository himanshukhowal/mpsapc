import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IWaiver, Waiver } from 'app/shared/model/waiver.model';
import { WaiverService } from './waiver.service';

@Component({
  selector: 'jhi-waiver-update',
  templateUrl: './waiver-update.component.html'
})
export class WaiverUpdateComponent implements OnInit {
  isSaving = false;
  dateCreatedDp: any;
  dateModifiedDp: any;

  editForm = this.fb.group({
    id: [],
    waiverType: [null, [Validators.required]],
    entityName: [null, [Validators.required]],
    activeStatus: [null, [Validators.required]],
    dateCreated: [],
    dateModified: []
  });

  constructor(protected waiverService: WaiverService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ waiver }) => {
      this.updateForm(waiver);
    });
  }

  updateForm(waiver: IWaiver): void {
    this.editForm.patchValue({
      id: waiver.id,
      waiverType: waiver.waiverType,
      entityName: waiver.entityName,
      activeStatus: waiver.activeStatus,
      dateCreated: waiver.dateCreated,
      dateModified: waiver.dateModified
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const waiver = this.createFromForm();
    if (waiver.id !== undefined) {
      this.subscribeToSaveResponse(this.waiverService.update(waiver));
    } else {
      this.subscribeToSaveResponse(this.waiverService.create(waiver));
    }
  }

  private createFromForm(): IWaiver {
    return {
      ...new Waiver(),
      id: this.editForm.get(['id'])!.value,
      waiverType: this.editForm.get(['waiverType'])!.value,
      entityName: this.editForm.get(['entityName'])!.value,
      activeStatus: this.editForm.get(['activeStatus'])!.value,
      dateCreated: this.editForm.get(['dateCreated'])!.value,
      dateModified: this.editForm.get(['dateModified'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IWaiver>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }
}
