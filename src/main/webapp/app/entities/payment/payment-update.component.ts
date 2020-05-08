import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IPayment, Payment } from 'app/shared/model/payment.model';
import { PaymentService } from './payment.service';

@Component({
  selector: 'jhi-payment-update',
  templateUrl: './payment-update.component.html'
})
export class PaymentUpdateComponent implements OnInit {
  isSaving = false;
  dateCreatedDp: any;
  dateModifiedDp: any;

  editForm = this.fb.group({
    id: [],
    currency: [null, [Validators.required]],
    amount: [null, [Validators.required, Validators.min(0)]],
    dateCreated: [],
    dateModified: []
  });

  constructor(protected paymentService: PaymentService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ payment }) => {
      this.updateForm(payment);
    });
  }

  updateForm(payment: IPayment): void {
    this.editForm.patchValue({
      id: payment.id,
      currency: payment.currency,
      amount: payment.amount,
      dateCreated: payment.dateCreated,
      dateModified: payment.dateModified
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const payment = this.createFromForm();
    if (payment.id !== undefined) {
      this.subscribeToSaveResponse(this.paymentService.update(payment));
    } else {
      this.subscribeToSaveResponse(this.paymentService.create(payment));
    }
  }

  private createFromForm(): IPayment {
    return {
      ...new Payment(),
      id: this.editForm.get(['id'])!.value,
      currency: this.editForm.get(['currency'])!.value,
      amount: this.editForm.get(['amount'])!.value,
      dateCreated: this.editForm.get(['dateCreated'])!.value,
      dateModified: this.editForm.get(['dateModified'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPayment>>): void {
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
