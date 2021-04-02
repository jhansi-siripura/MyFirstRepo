import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { GratitudeComponent } from './list/gratitude.component';
import { GratitudeDetailComponent } from './detail/gratitude-detail.component';
import { GratitudeUpdateComponent } from './update/gratitude-update.component';
import { GratitudeDeleteDialogComponent } from './delete/gratitude-delete-dialog.component';
import { GratitudeRoutingModule } from './route/gratitude-routing.module';

@NgModule({
  imports: [SharedModule, GratitudeRoutingModule],
  declarations: [GratitudeComponent, GratitudeDetailComponent, GratitudeUpdateComponent, GratitudeDeleteDialogComponent],
  entryComponents: [GratitudeDeleteDialogComponent],
})
export class GratitudeModule {}
