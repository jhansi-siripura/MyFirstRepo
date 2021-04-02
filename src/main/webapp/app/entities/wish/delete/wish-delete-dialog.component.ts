import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IWish } from '../wish.model';
import { WishService } from '../service/wish.service';

@Component({
  templateUrl: './wish-delete-dialog.component.html',
})
export class WishDeleteDialogComponent {
  wish?: IWish;

  constructor(protected wishService: WishService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.wishService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
