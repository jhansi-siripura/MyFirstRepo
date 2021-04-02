import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { TradeWishComponent } from './list/trade-wish.component';
import { TradeWishDetailComponent } from './detail/trade-wish-detail.component';
import { TradeWishUpdateComponent } from './update/trade-wish-update.component';
import { TradeWishDeleteDialogComponent } from './delete/trade-wish-delete-dialog.component';
import { TradeWishRoutingModule } from './route/trade-wish-routing.module';

@NgModule({
  imports: [SharedModule, TradeWishRoutingModule],
  declarations: [TradeWishComponent, TradeWishDetailComponent, TradeWishUpdateComponent, TradeWishDeleteDialogComponent],
  entryComponents: [TradeWishDeleteDialogComponent],
})
export class TradeWishModule {}
