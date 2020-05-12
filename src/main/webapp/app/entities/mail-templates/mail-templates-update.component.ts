import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IMailTemplates, MailTemplates } from 'app/shared/model/mail-templates.model';
import { MailTemplatesService } from './mail-templates.service';

@Component({
  selector: 'jhi-mail-templates-update',
  templateUrl: './mail-templates-update.component.html'
})
export class MailTemplatesUpdateComponent implements OnInit {
  isSaving = false;
  dateCreatedDp: any;
  dateModifiedDp: any;

  editForm = this.fb.group({
    id: [],
    templateName: [null, [Validators.required]],
    mailCC: [],
    mailBCC: [],
    mailSubject: [],
    mailBody: [],
    dateCreated: [],
    dateModified: []
  });

  constructor(protected mailTemplatesService: MailTemplatesService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ mailTemplates }) => {
      this.updateForm(mailTemplates);
    });
  }

  updateForm(mailTemplates: IMailTemplates): void {
    this.editForm.patchValue({
      id: mailTemplates.id,
      templateName: mailTemplates.templateName,
      mailCC: mailTemplates.mailCC,
      mailBCC: mailTemplates.mailBCC,
      mailSubject: mailTemplates.mailSubject,
      mailBody: mailTemplates.mailBody,
      dateCreated: mailTemplates.dateCreated,
      dateModified: mailTemplates.dateModified
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const mailTemplates = this.createFromForm();
    if (mailTemplates.id !== undefined) {
      this.subscribeToSaveResponse(this.mailTemplatesService.update(mailTemplates));
    } else {
      this.subscribeToSaveResponse(this.mailTemplatesService.create(mailTemplates));
    }
  }

  private createFromForm(): IMailTemplates {
    return {
      ...new MailTemplates(),
      id: this.editForm.get(['id'])!.value,
      templateName: this.editForm.get(['templateName'])!.value,
      mailCC: this.editForm.get(['mailCC'])!.value,
      mailBCC: this.editForm.get(['mailBCC'])!.value,
      mailSubject: this.editForm.get(['mailSubject'])!.value,
      mailBody: this.editForm.get(['mailBody'])!.value,
      dateCreated: this.editForm.get(['dateCreated'])!.value,
      dateModified: this.editForm.get(['dateModified'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMailTemplates>>): void {
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
