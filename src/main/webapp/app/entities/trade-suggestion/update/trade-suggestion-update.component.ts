import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ITradeSuggestion, TradeSuggestion } from '../trade-suggestion.model';
import { TradeSuggestionService } from '../service/trade-suggestion.service';

@Component({
  selector: 'jhi-trade-suggestion-update',
  templateUrl: './trade-suggestion-update.component.html',
})
export class TradeSuggestionUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
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
    currentMarketPrice: [],
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
    this.editForm.patchValue({
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
      id: this.editForm.get(['id'])!.value,
      action: this.editForm.get(['action'])!.value,
      tradeInPrice: this.editForm.get(['tradeInPrice'])!.value,
      minTradeOutPrice: this.editForm.get(['minTradeOutPrice'])!.value,
      minProfitPoints: this.editForm.get(['minProfitPoints'])!.value,
      betterTradeoutPrice: this.editForm.get(['betterTradeoutPrice'])!.value,
      betterTradeOutProfitPoints: this.editForm.get(['betterTradeOutProfitPoints'])!.value,
      actualTradeoutPrice: this.editForm.get(['actualTradeoutPrice'])!.value,
      actualProfitPoints: this.editForm.get(['actualProfitPoints'])!.value,
      slPoints: this.editForm.get(['slPoints'])!.value,
      tradeStatus: this.editForm.get(['tradeStatus'])!.value,
      tradeResults: this.editForm.get(['tradeResults'])!.value,
      tradeInTime: this.editForm.get(['tradeInTime'])!.value
        ? dayjs(this.editForm.get(['tradeInTime'])!.value, DATE_TIME_FORMAT)
        : undefined,
      tradeOutTime: this.editForm.get(['tradeOutTime'])!.value
        ? dayjs(this.editForm.get(['tradeOutTime'])!.value, DATE_TIME_FORMAT)
        : undefined,
      tradeDuration: this.editForm.get(['tradeDuration'])!.value,
      tradeDate: this.editForm.get(['tradeDate'])!.value,
      tradeSuggestionTime: this.editForm.get(['tradeSuggestionTime'])!.value
        ? dayjs(this.editForm.get(['tradeSuggestionTime'])!.value, DATE_TIME_FORMAT)
        : undefined,
      tradeType: this.editForm.get(['tradeType'])!.value,
      actualPL: this.editForm.get(['actualPL'])!.value,
      slPrice: this.editForm.get(['slPrice'])!.value,
      currentMarketPrice: this.editForm.get(['currentMarketPrice'])!.value,
    };
  }
}
