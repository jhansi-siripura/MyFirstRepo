import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IGratitude } from '../gratitude.model';
import { GratitudeService } from '../service/gratitude.service';

@Component({
  templateUrl: './gratitude-delete-dialog.component.html',
})
export class GratitudeDeleteDialogComponent {
  gratitude?: IGratitude;

  constructor(protected gratitudeService: GratitudeService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.gratitudeService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
