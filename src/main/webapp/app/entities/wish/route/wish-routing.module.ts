import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { WishComponent } from '../list/wish.component';
import { WishDetailComponent } from '../detail/wish-detail.component';
import { WishUpdateComponent } from '../update/wish-update.component';
import { WishRoutingResolveService } from './wish-routing-resolve.service';

const wishRoute: Routes = [
  {
    path: '',
    component: WishComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: WishDetailComponent,
    resolve: {
      wish: WishRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: WishUpdateComponent,
    resolve: {
      wish: WishRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: WishUpdateComponent,
    resolve: {
      wish: WishRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(wishRoute)],
  exports: [RouterModule],
})
export class WishRoutingModule {}
