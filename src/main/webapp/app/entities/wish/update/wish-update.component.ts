import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IWish, Wish } from '../wish.model';
import { WishService } from '../service/wish.service';

@Component({
  selector: 'jhi-wish-update',
  templateUrl: './wish-update.component.html',
})
export class WishUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    myWishNote: [null, [Validators.required]],
    startDate: [],
    fulfilled: [],
    fulfilledDate: [],
    duration: [],
    category: [],
  });

  constructor(protected wishService: WishService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ wish }) => {
      this.updateForm(wish);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const wish = this.createFromForm();
    if (wish.id !== undefined) {
      this.subscribeToSaveResponse(this.wishService.update(wish));
    } else {
      this.subscribeToSaveResponse(this.wishService.create(wish));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IWish>>): void {
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

  protected updateForm(wish: IWish): void {
    this.editForm.patchValue({
      id: wish.id,
      myWishNote: wish.myWishNote,
      startDate: wish.startDate,
      fulfilled: wish.fulfilled,
      fulfilledDate: wish.fulfilledDate,
      duration: wish.duration,
      category: wish.category,
    });
  }

  protected createFromForm(): IWish {
    return {
      ...new Wish(),
      id: this.editForm.get(['id'])!.value,
      myWishNote: this.editForm.get(['myWishNote'])!.value,
      startDate: this.editForm.get(['startDate'])!.value,
      fulfilled: this.editForm.get(['fulfilled'])!.value,
      fulfilledDate: this.editForm.get(['fulfilledDate'])!.value,
      duration: this.editForm.get(['duration'])!.value,
      category: this.editForm.get(['category'])!.value,
    };
  }
}
