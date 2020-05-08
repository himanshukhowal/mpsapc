import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IContactUs, ContactUs } from 'app/shared/model/contact-us.model';
import { ContactUsService } from './contact-us.service';

@Component({
  selector: 'jhi-contact-us-update',
  templateUrl: './contact-us-update.component.html'
})
export class ContactUsUpdateComponent implements OnInit {
  isSaving = false;
  dateAddedDp: any;
  dateModifiedDp: any;

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    email: [null, [Validators.required]],
    contact: [null, [Validators.required]],
    message: [null, [Validators.required]],
    dateAdded: [],
    dateModified: []
  });

  constructor(protected contactUsService: ContactUsService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ contactUs }) => {
      this.updateForm(contactUs);
    });
  }

  updateForm(contactUs: IContactUs): void {
    this.editForm.patchValue({
      id: contactUs.id,
      name: contactUs.name,
      email: contactUs.email,
      contact: contactUs.contact,
      message: contactUs.message,
      dateAdded: contactUs.dateAdded,
      dateModified: contactUs.dateModified
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const contactUs = this.createFromForm();
    if (contactUs.id !== undefined) {
      this.subscribeToSaveResponse(this.contactUsService.update(contactUs));
    } else {
      this.subscribeToSaveResponse(this.contactUsService.create(contactUs));
    }
  }

  private createFromForm(): IContactUs {
    return {
      ...new ContactUs(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      email: this.editForm.get(['email'])!.value,
      contact: this.editForm.get(['contact'])!.value,
      message: this.editForm.get(['message'])!.value,
      dateAdded: this.editForm.get(['dateAdded'])!.value,
      dateModified: this.editForm.get(['dateModified'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IContactUs>>): void {
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
