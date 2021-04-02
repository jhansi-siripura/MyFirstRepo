import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { GratitudeComponent } from '../list/gratitude.component';
import { GratitudeDetailComponent } from '../detail/gratitude-detail.component';
import { GratitudeUpdateComponent } from '../update/gratitude-update.component';
import { GratitudeRoutingResolveService } from './gratitude-routing-resolve.service';

const gratitudeRoute: Routes = [
  {
    path: '',
    component: GratitudeComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: GratitudeDetailComponent,
    resolve: {
      gratitude: GratitudeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: GratitudeUpdateComponent,
    resolve: {
      gratitude: GratitudeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: GratitudeUpdateComponent,
    resolve: {
      gratitude: GratitudeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(gratitudeRoute)],
  exports: [RouterModule],
})
export class GratitudeRoutingModule {}
