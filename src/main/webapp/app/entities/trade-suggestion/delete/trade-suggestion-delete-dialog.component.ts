import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITradeSuggestion } from '../trade-suggestion.model';
import { TradeSuggestionService } from '../service/trade-suggestion.service';

@Component({
  templateUrl: './trade-suggestion-delete-dialog.component.html',
})
export class TradeSuggestionDeleteDialogComponent {
  tradeSuggestion?: ITradeSuggestion;

  constructor(protected tradeSuggestionService: TradeSuggestionService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.tradeSuggestionService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
