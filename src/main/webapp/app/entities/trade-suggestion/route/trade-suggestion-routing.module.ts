import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TradeSuggestionComponent } from '../list/trade-suggestion.component';
import { TradeSuggestionDetailComponent } from '../detail/trade-suggestion-detail.component';
import { TradeSuggestionUpdateComponent } from '../update/trade-suggestion-update.component';
import { TradeSuggestionRoutingResolveService } from './trade-suggestion-routing-resolve.service';

const tradeSuggestionRoute: Routes = [
  {
    path: '',
    component: TradeSuggestionComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TradeSuggestionDetailComponent,
    resolve: {
      tradeSuggestion: TradeSuggestionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TradeSuggestionUpdateComponent,
    resolve: {
      tradeSuggestion: TradeSuggestionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TradeSuggestionUpdateComponent,
    resolve: {
      tradeSuggestion: TradeSuggestionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(tradeSuggestionRoute)],
  exports: [RouterModule],
})
export class TradeSuggestionRoutingModule {}
