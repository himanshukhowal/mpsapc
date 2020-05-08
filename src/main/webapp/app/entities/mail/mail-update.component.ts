import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IMail, Mail } from 'app/shared/model/mail.model';
import { MailService } from './mail.service';
import { IJournal } from 'app/shared/model/journal.model';
import { JournalService } from 'app/entities/journal/journal.service';
import { IMailTemplates } from 'app/shared/model/mail-templates.model';
import { MailTemplatesService } from 'app/entities/mail-templates/mail-templates.service';

type SelectableEntity = IJournal | IMailTemplates;

@Component({
  selector: 'jhi-mail-update',
  templateUrl: './mail-update.component.html'
})
export class MailUpdateComponent implements OnInit {
  isSaving = false;
  journals: IJournal[] = [];
  mailtemplates: IMailTemplates[] = [];
  dateCreatedDp: any;
  dateModifiedDp: any;

  editForm = this.fb.group({
    id: [],
    mailConfigurationName: [null, [Validators.required]],
    stageName: [null, [Validators.required]],
    activeStatus: [null, [Validators.required]],
    dateCreated: [],
    dateModified: [],
    associatedJournal: [null, Validators.required],
    associatedMailTemplate: [null, Validators.required]
  });

  constructor(
    protected mailService: MailService,
    protected journalService: JournalService,
    protected mailTemplatesService: MailTemplatesService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ mail }) => {
      this.updateForm(mail);

      this.journalService.query().subscribe((res: HttpResponse<IJournal[]>) => (this.journals = res.body || []));

      this.mailTemplatesService.query().subscribe((res: HttpResponse<IMailTemplates[]>) => (this.mailtemplates = res.body || []));
    });
  }

  updateForm(mail: IMail): void {
    this.editForm.patchValue({
      id: mail.id,
      mailConfigurationName: mail.mailConfigurationName,
      stageName: mail.stageName,
      activeStatus: mail.activeStatus,
      dateCreated: mail.dateCreated,
      dateModified: mail.dateModified,
      associatedJournal: mail.associatedJournal,
      associatedMailTemplate: mail.associatedMailTemplate
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const mail = this.createFromForm();
    if (mail.id !== undefined) {
      this.subscribeToSaveResponse(this.mailService.update(mail));
    } else {
      this.subscribeToSaveResponse(this.mailService.create(mail));
    }
  }

  private createFromForm(): IMail {
    return {
      ...new Mail(),
      id: this.editForm.get(['id'])!.value,
      mailConfigurationName: this.editForm.get(['mailConfigurationName'])!.value,
      stageName: this.editForm.get(['stageName'])!.value,
      activeStatus: this.editForm.get(['activeStatus'])!.value,
      dateCreated: this.editForm.get(['dateCreated'])!.value,
      dateModified: this.editForm.get(['dateModified'])!.value,
      associatedJournal: this.editForm.get(['associatedJournal'])!.value,
      associatedMailTemplate: this.editForm.get(['associatedMailTemplate'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMail>>): void {
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

  trackById(index: number, item: SelectableEntity): any {
    return item.id;
  }
}
