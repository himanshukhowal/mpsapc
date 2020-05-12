import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IDiscount, Discount } from 'app/shared/model/discount.model';
import { DiscountService } from './discount.service';

@Component({
  selector: 'jhi-discount-update',
  templateUrl: './discount-update.component.html'
})
export class DiscountUpdateComponent implements OnInit {
  isSaving = false;
  dateFromDp: any;
  dateToDp: any;
  dateCreatedDp: any;
  dateModifiedDp: any;

  editForm = this.fb.group({
    id: [],
    discountType: [null, [Validators.required]],
    entityName: [null, [Validators.required]],
    amount: [null, [Validators.required]],
    dateFrom: [],
    dateTo: [],
    activeStatus: [null, [Validators.required]],
    dateCreated: [],
    dateModified: []
  });

  constructor(protected discountService: DiscountService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ discount }) => {
      this.updateForm(discount);
    });
  }

  updateForm(discount: IDiscount): void {
    this.editForm.patchValue({
      id: discount.id,
      discountType: discount.discountType,
      entityName: discount.entityName,
      amount: discount.amount,
      dateFrom: discount.dateFrom,
      dateTo: discount.dateTo,
      activeStatus: discount.activeStatus,
      dateCreated: discount.dateCreated,
      dateModified: discount.dateModified
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const discount = this.createFromForm();
    if (discount.id !== undefined) {
      this.subscribeToSaveResponse(this.discountService.update(discount));
    } else {
      this.subscribeToSaveResponse(this.discountService.create(discount));
    }
  }

  private createFromForm(): IDiscount {
    return {
      ...new Discount(),
      id: this.editForm.get(['id'])!.value,
      discountType: this.editForm.get(['discountType'])!.value,
      entityName: this.editForm.get(['entityName'])!.value,
      amount: this.editForm.get(['amount'])!.value,
      dateFrom: this.editForm.get(['dateFrom'])!.value,
      dateTo: this.editForm.get(['dateTo'])!.value,
      activeStatus: this.editForm.get(['activeStatus'])!.value,
      dateCreated: this.editForm.get(['dateCreated'])!.value,
      dateModified: this.editForm.get(['dateModified'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDiscount>>): void {
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
