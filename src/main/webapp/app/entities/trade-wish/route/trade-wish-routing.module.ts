import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TradeWishComponent } from '../list/trade-wish.component';
import { TradeWishDetailComponent } from '../detail/trade-wish-detail.component';
import { TradeWishUpdateComponent } from '../update/trade-wish-update.component';
import { TradeWishRoutingResolveService } from './trade-wish-routing-resolve.service';

const tradeWishRoute: Routes = [
  {
    path: '',
    component: TradeWishComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TradeWishDetailComponent,
    resolve: {
      tradeWish: TradeWishRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TradeWishUpdateComponent,
    resolve: {
      tradeWish: TradeWishRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TradeWishUpdateComponent,
    resolve: {
      tradeWish: TradeWishRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(tradeWishRoute)],
  exports: [RouterModule],
})
export class TradeWishRoutingModule {}
