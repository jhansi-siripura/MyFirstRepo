import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'gratitude',
        data: { pageTitle: 'Gratitudes' },
        loadChildren: () => import('./gratitude/gratitude.module').then(m => m.GratitudeModule),
      },
      {
        path: 'trade-wish',
        data: { pageTitle: 'TradeWishes' },
        loadChildren: () => import('./trade-wish/trade-wish.module').then(m => m.TradeWishModule),
      },
      {
        path: 'trade-suggestion',
        data: { pageTitle: 'TradeSuggestions' },
        loadChildren: () => import('./trade-suggestion/trade-suggestion.module').then(m => m.TradeSuggestionModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
