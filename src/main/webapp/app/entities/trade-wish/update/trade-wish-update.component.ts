import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ITradeWish, TradeWish } from '../trade-wish.model';
import { TradeWishService } from '../service/trade-wish.service';

@Component({
  selector: 'jhi-trade-wish-update',
  templateUrl: './trade-wish-update.component.html',
})
export class TradeWishUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    tradeWishNote: [null, [Validators.required]],
    picked: [],
    pickedDate: [null, [Validators.required]],
  });

  constructor(protected tradeWishService: TradeWishService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tradeWish }) => {
      this.updateForm(tradeWish);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const tradeWish = this.createFromForm();
    if (tradeWish.id !== undefined) {
      this.subscribeToSaveResponse(this.tradeWishService.update(tradeWish));
    } else {
      this.subscribeToSaveResponse(this.tradeWishService.create(tradeWish));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITradeWish>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(tradeWish: ITradeWish): void {
    this.editForm.patchValue({
      id: tradeWish.id,
      tradeWishNote: tradeWish.tradeWishNote,
      picked: tradeWish.picked,
      pickedDate: tradeWish.pickedDate,
    });
  }

  protected createFromForm(): ITradeWish {
    return {
      ...new TradeWish(),
      id: this.editForm.get(['id'])!.value,
      tradeWishNote: this.editForm.get(['tradeWishNote'])!.value,
      picked: this.editForm.get(['picked'])!.value,
      pickedDate: this.editForm.get(['pickedDate'])!.value,
    };
  }
}
