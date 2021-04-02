import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IGratitude, Gratitude } from '../gratitude.model';
import { GratitudeService } from '../service/gratitude.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-gratitude-update',
  templateUrl: './gratitude-update.component.html',
})
export class GratitudeUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    createdDate: [null, [Validators.required]],
    loved: [],
    achieved: [],
    gratefulNote: [null, [Validators.required]],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected gratitudeService: GratitudeService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ gratitude }) => {
      this.updateForm(gratitude);
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(
          new EventWithContent<AlertError>('krishnaGratitudeApp.error', { message: err.message })
        ),
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
      createdDate: gratitude.createdDate,
      loved: gratitude.loved,
      achieved: gratitude.achieved,
      gratefulNote: gratitude.gratefulNote,
    });
  }

  protected createFromForm(): IGratitude {
    return {
      ...new Gratitude(),
      id: this.editForm.get(['id'])!.value,
      createdDate: this.editForm.get(['createdDate'])!.value,
      loved: this.editForm.get(['loved'])!.value,
      achieved: this.editForm.get(['achieved'])!.value,
      gratefulNote: this.editForm.get(['gratefulNote'])!.value,
    };
  }
}
