import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { WishComponent } from './list/wish.component';
import { WishDetailComponent } from './detail/wish-detail.component';
import { WishUpdateComponent } from './update/wish-update.component';
import { WishDeleteDialogComponent } from './delete/wish-delete-dialog.component';
import { WishRoutingModule } from './route/wish-routing.module';

@NgModule({
  imports: [SharedModule, WishRoutingModule],
  declarations: [WishComponent, WishDetailComponent, WishUpdateComponent, WishDeleteDialogComponent],
  entryComponents: [WishDeleteDialogComponent],
})
export class WishModule {}
