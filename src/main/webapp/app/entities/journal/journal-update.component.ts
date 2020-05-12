import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IJournal, Journal } from 'app/shared/model/journal.model';
import { JournalService } from './journal.service';

@Component({
  selector: 'jhi-journal-update',
  templateUrl: './journal-update.component.html'
})
export class JournalUpdateComponent implements OnInit {
  isSaving = false;
  dateCreatedDp: any;
  dateModifiedDp: any;

  editForm = this.fb.group({
    id: [],
    journalAcronym: [null, [Validators.required]],
    journalTitle: [null, [Validators.required]],
    activeStatus: [null, [Validators.required]],
    dateCreated: [],
    dateModified: []
  });

  constructor(protected journalService: JournalService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ journal }) => {
      this.updateForm(journal);
    });
  }

  updateForm(journal: IJournal): void {
    this.editForm.patchValue({
      id: journal.id,
      journalAcronym: journal.journalAcronym,
      journalTitle: journal.journalTitle,
      activeStatus: journal.activeStatus,
      dateCreated: journal.dateCreated,
      dateModified: journal.dateModified
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const journal = this.createFromForm();
    if (journal.id !== undefined) {
      this.subscribeToSaveResponse(this.journalService.update(journal));
    } else {
      this.subscribeToSaveResponse(this.journalService.create(journal));
    }
  }

  private createFromForm(): IJournal {
    return {
      ...new Journal(),
      id: this.editForm.get(['id'])!.value,
      journalAcronym: this.editForm.get(['journalAcronym'])!.value,
      journalTitle: this.editForm.get(['journalTitle'])!.value,
      activeStatus: this.editForm.get(['activeStatus'])!.value,
      dateCreated: this.editForm.get(['dateCreated'])!.value,
      dateModified: this.editForm.get(['dateModified'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IJournal>>): void {
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
