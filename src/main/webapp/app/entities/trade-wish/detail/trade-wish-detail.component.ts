import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITradeWish } from '../trade-wish.model';

@Component({
  selector: 'jhi-trade-wish-detail',
  templateUrl: './trade-wish-detail.component.html',
})
export class TradeWishDetailComponent implements OnInit {
  tradeWish: ITradeWish | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tradeWish }) => {
      this.tradeWish = tradeWish;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
