import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { IManuscript, Manuscript } from 'app/shared/model/manuscript.model';
import { ManuscriptService } from './manuscript.service';
import { IPayment } from 'app/shared/model/payment.model';
import { PaymentService } from 'app/entities/payment/payment.service';
import { IJournal } from 'app/shared/model/journal.model';
import { JournalService } from 'app/entities/journal/journal.service';
import { IAuthor } from 'app/shared/model/author.model';
import { AuthorService } from 'app/entities/author/author.service';

type SelectableEntity = IPayment | IJournal | IAuthor;

@Component({
  selector: 'jhi-manuscript-update',
  templateUrl: './manuscript-update.component.html'
})
export class ManuscriptUpdateComponent implements OnInit {
  isSaving = false;
  linkedpayments: IPayment[] = [];
  journals: IJournal[] = [];
  authors: IAuthor[] = [];
  dateCreatedDp: any;
  dateModifiedDp: any;

  editForm = this.fb.group({
    id: [],
    manuscriptId: [null, [Validators.required]],
    manuscriptTitle: [null, [Validators.required]],
    apcStatus: [null, [Validators.required]],
    dateCreated: [],
    dateModified: [],
    linkedPayment: [null, Validators.required],
    manuscriptJournalAcronym: [null, Validators.required],
    manuscriptAuthorName: [null, Validators.required]
  });

  constructor(
    protected manuscriptService: ManuscriptService,
    protected paymentService: PaymentService,
    protected journalService: JournalService,
    protected authorService: AuthorService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ manuscript }) => {
      this.updateForm(manuscript);

      this.paymentService
        .query({ filter: 'manuscript-is-null' })
        .pipe(
          map((res: HttpResponse<IPayment[]>) => {
            return res.body || [];
          })
        )
        .subscribe((resBody: IPayment[]) => {
          if (!manuscript.linkedPayment || !manuscript.linkedPayment.id) {
            this.linkedpayments = resBody;
          } else {
            this.paymentService
              .find(manuscript.linkedPayment.id)
              .pipe(
                map((subRes: HttpResponse<IPayment>) => {
                  return subRes.body ? [subRes.body].concat(resBody) : resBody;
                })
              )
              .subscribe((concatRes: IPayment[]) => (this.linkedpayments = concatRes));
          }
        });

      this.journalService.query().subscribe((res: HttpResponse<IJournal[]>) => (this.journals = res.body || []));

      this.authorService.query().subscribe((res: HttpResponse<IAuthor[]>) => (this.authors = res.body || []));
    });
  }

  updateForm(manuscript: IManuscript): void {
    this.editForm.patchValue({
      id: manuscript.id,
      manuscriptId: manuscript.manuscriptId,
      manuscriptTitle: manuscript.manuscriptTitle,
      apcStatus: manuscript.apcStatus,
      dateCreated: manuscript.dateCreated,
      dateModified: manuscript.dateModified,
      linkedPayment: manuscript.linkedPayment,
      manuscriptJournalAcronym: manuscript.manuscriptJournalAcronym,
      manuscriptAuthorName: manuscript.manuscriptAuthorName
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const manuscript = this.createFromForm();
    if (manuscript.id !== undefined) {
      this.subscribeToSaveResponse(this.manuscriptService.update(manuscript));
    } else {
      this.subscribeToSaveResponse(this.manuscriptService.create(manuscript));
    }
  }

  private createFromForm(): IManuscript {
    return {
      ...new Manuscript(),
      id: this.editForm.get(['id'])!.value,
      manuscriptId: this.editForm.get(['manuscriptId'])!.value,
      manuscriptTitle: this.editForm.get(['manuscriptTitle'])!.value,
      apcStatus: this.editForm.get(['apcStatus'])!.value,
      dateCreated: this.editForm.get(['dateCreated'])!.value,
      dateModified: this.editForm.get(['dateModified'])!.value,
      linkedPayment: this.editForm.get(['linkedPayment'])!.value,
      manuscriptJournalAcronym: this.editForm.get(['manuscriptJournalAcronym'])!.value,
      manuscriptAuthorName: this.editForm.get(['manuscriptAuthorName'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IManuscript>>): void {
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
