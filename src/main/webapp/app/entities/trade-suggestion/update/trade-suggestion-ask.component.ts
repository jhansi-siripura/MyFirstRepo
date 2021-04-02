import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ITradeSuggestion, TradeSuggestion } from '../trade-suggestion.model';
import { TradeSuggestionService } from '../service/trade-suggestion.service';

@Component({
  selector: 'jhi-trade-suggestion-ask',
  templateUrl: './trade-suggestion-ask.component.html',
})
export class TradeSuggestionAskComponent implements OnInit {
  isSaving = false;

  askForm = this.fb.group({
    id: [],
    action: [],
    tradeInPrice: [],
    minTradeOutPrice: [],
    minProfitPoints: [],
    betterTradeoutPrice: [],
    betterTradeOutProfitPoints: [],
    actualTradeoutPrice: [],
    actualProfitPoints: [],
    slPoints: [],
    tradeStatus: [],
    tradeResults: [],
    tradeInTime: [],
    tradeOutTime: [],
    tradeDuration: [],
    tradeDate: [],
    tradeSuggestionTime: [],
    tradeType: [],
    actualPL: [],
    slPrice: [],
    currentMarketPrice: [null, [Validators.required, Validators.min(1000), Validators.max(9999)]],
  });

  constructor(
    protected tradeSuggestionService: TradeSuggestionService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tradeSuggestion }) => {
      if (tradeSuggestion.id === undefined) {
        const today = dayjs().startOf('day');
        tradeSuggestion.tradeInTime = today;
        tradeSuggestion.tradeOutTime = today;
        tradeSuggestion.tradeSuggestionTime = today;
      }

      this.updateForm(tradeSuggestion);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const tradeSuggestion = this.createFromForm();
    if (tradeSuggestion.id !== undefined) {
      this.subscribeToSaveResponse(this.tradeSuggestionService.update(tradeSuggestion));
    } else {
      this.subscribeToSaveResponse(this.tradeSuggestionService.create(tradeSuggestion));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITradeSuggestion>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(tradeSuggestion: ITradeSuggestion): void {
    this.askForm.patchValue({
      id: tradeSuggestion.id,
      action: tradeSuggestion.action,
      tradeInPrice: tradeSuggestion.tradeInPrice,
      minTradeOutPrice: tradeSuggestion.minTradeOutPrice,
      minProfitPoints: tradeSuggestion.minProfitPoints,
      betterTradeoutPrice: tradeSuggestion.betterTradeoutPrice,
      betterTradeOutProfitPoints: tradeSuggestion.betterTradeOutProfitPoints,
      actualTradeoutPrice: tradeSuggestion.actualTradeoutPrice,
      actualProfitPoints: tradeSuggestion.actualProfitPoints,
      slPoints: tradeSuggestion.slPoints,
      tradeStatus: tradeSuggestion.tradeStatus,
      tradeResults: tradeSuggestion.tradeResults,
      tradeInTime: tradeSuggestion.tradeInTime ? tradeSuggestion.tradeInTime.format(DATE_TIME_FORMAT) : null,
      tradeOutTime: tradeSuggestion.tradeOutTime ? tradeSuggestion.tradeOutTime.format(DATE_TIME_FORMAT) : null,
      tradeDuration: tradeSuggestion.tradeDuration,
      tradeDate: tradeSuggestion.tradeDate,
      tradeSuggestionTime: tradeSuggestion.tradeSuggestionTime ? tradeSuggestion.tradeSuggestionTime.format(DATE_TIME_FORMAT) : null,
      tradeType: tradeSuggestion.tradeType,
      actualPL: tradeSuggestion.actualPL,
      slPrice: tradeSuggestion.slPrice,
      currentMarketPrice: tradeSuggestion.currentMarketPrice,
    });
  }

  protected createFromForm(): ITradeSuggestion {
    return {
      ...new TradeSuggestion(),
      id: this.askForm.get(['id'])!.value,
      action: this.askForm.get(['action'])!.value,
      tradeInPrice: this.askForm.get(['tradeInPrice'])!.value,
      minTradeOutPrice: this.askForm.get(['minTradeOutPrice'])!.value,
      minProfitPoints: this.askForm.get(['minProfitPoints'])!.value,
      betterTradeoutPrice: this.askForm.get(['betterTradeoutPrice'])!.value,
      betterTradeOutProfitPoints: this.askForm.get(['betterTradeOutProfitPoints'])!.value,
      actualTradeoutPrice: this.askForm.get(['actualTradeoutPrice'])!.value,
      actualProfitPoints: this.askForm.get(['actualProfitPoints'])!.value,
      slPoints: this.askForm.get(['slPoints'])!.value,
      tradeStatus: this.askForm.get(['tradeStatus'])!.value,
      tradeResults: this.askForm.get(['tradeResults'])!.value,
      tradeInTime: this.askForm.get(['tradeInTime'])!.value ? dayjs(this.askForm.get(['tradeInTime'])!.value, DATE_TIME_FORMAT) : undefined,
      tradeOutTime: this.askForm.get(['tradeOutTime'])!.value
        ? dayjs(this.askForm.get(['tradeOutTime'])!.value, DATE_TIME_FORMAT)
        : undefined,
      tradeDuration: this.askForm.get(['tradeDuration'])!.value,
      tradeDate: this.askForm.get(['tradeDate'])!.value,
      tradeSuggestionTime: this.askForm.get(['tradeSuggestionTime'])!.value
        ? dayjs(this.askForm.get(['tradeSuggestionTime'])!.value, DATE_TIME_FORMAT)
        : undefined,
      tradeType: this.askForm.get(['tradeType'])!.value,
      actualPL: this.askForm.get(['actualPL'])!.value,
      slPrice: this.askForm.get(['slPrice'])!.value,
      currentMarketPrice: this.askForm.get(['currentMarketPrice'])!.value,
    };
  }
}
