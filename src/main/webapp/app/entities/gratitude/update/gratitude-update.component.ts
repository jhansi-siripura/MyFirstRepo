import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IGratitude, Gratitude } from '../gratitude.model';
import { GratitudeService } from '../service/gratitude.service';

@Component({
  selector: 'jhi-gratitude-update',
  templateUrl: './gratitude-update.component.html',
})
export class GratitudeUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    gratefulNote: [null, [Validators.required]],
    createdDate: [null, [Validators.required]],
    loved: [],
    achieved: [],
  });

  constructor(protected gratitudeService: GratitudeService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ gratitude }) => {
      if (gratitude.id === undefined) {
        const today = dayjs().startOf('day');
        gratitude.createdDate = today;
      }

      this.updateForm(gratitude);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const gratitude = this.createFromForm();
    if (gratitude.id !== undefined) {
      this.subscribeToSaveResponse(this.gratitudeService.update(gratitude));
    } else {
      this.subscribeToSaveResponse(this.gratitudeService.create(gratitude));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IGratitude>>): void {
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

  protected updateForm(gratitude: IGratitude): void {
    this.editForm.patchValue({
      id: gratitude.id,
      gratefulNote: gratitude.gratefulNote,
      createdDate: gratitude.createdDate ? gratitude.createdDate.format(DATE_TIME_FORMAT) : null,
      loved: gratitude.loved,
      achieved: gratitude.achieved,
    });
  }

  protected createFromForm(): IGratitude {
    return {
      ...new Gratitude(),
      id: this.editForm.get(['id'])!.value,
      gratefulNote: this.editForm.get(['gratefulNote'])!.value,
      createdDate: this.editForm.get(['createdDate'])!.value
        ? dayjs(this.editForm.get(['createdDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      loved: this.editForm.get(['loved'])!.value,
      achieved: this.editForm.get(['achieved'])!.value,
    };
  }
}
