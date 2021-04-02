import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITradeWish } from '../trade-wish.model';
import { TradeWishService } from '../service/trade-wish.service';

@Component({
  templateUrl: './trade-wish-delete-dialog.component.html',
})
export class TradeWishDeleteDialogComponent {
  tradeWish?: ITradeWish;

  constructor(protected tradeWishService: TradeWishService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.tradeWishService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
