import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { TradeSuggestionComponent } from './list/trade-suggestion.component';
import { TradeSuggestionDetailComponent } from './detail/trade-suggestion-detail.component';
import { TradeSuggestionUpdateComponent } from './update/trade-suggestion-update.component';
import { TradeSuggestionDeleteDialogComponent } from './delete/trade-suggestion-delete-dialog.component';
import { TradeSuggestionRoutingModule } from './route/trade-suggestion-routing.module';
import { TradeSuggestionAskComponent } from './update/trade-suggestion-ask.component';

@NgModule({
  imports: [SharedModule, TradeSuggestionRoutingModule],
  declarations: [
    TradeSuggestionComponent,
    TradeSuggestionDetailComponent,
    TradeSuggestionUpdateComponent,
    TradeSuggestionDeleteDialogComponent,
    TradeSuggestionAskComponent,
  ],
  entryComponents: [TradeSuggestionDeleteDialogComponent],
})
export class TradeSuggestionModule {}
