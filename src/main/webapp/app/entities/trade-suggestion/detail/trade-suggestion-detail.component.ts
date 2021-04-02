import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITradeSuggestion } from '../trade-suggestion.model';

@Component({
  selector: 'jhi-trade-suggestion-detail',
  templateUrl: './trade-suggestion-detail.component.html',
})
export class TradeSuggestionDetailComponent implements OnInit {
  tradeSuggestion: ITradeSuggestion | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tradeSuggestion }) => {
      this.tradeSuggestion = tradeSuggestion;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
