import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IWish } from '../wish.model';

@Component({
  selector: 'jhi-wish-detail',
  templateUrl: './wish-detail.component.html',
})
export class WishDetailComponent implements OnInit {
  wish: IWish | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ wish }) => {
      this.wish = wish;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
