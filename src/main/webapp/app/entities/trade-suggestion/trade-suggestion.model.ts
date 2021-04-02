import * as dayjs from 'dayjs';
import { TradeAction } from 'app/entities/enumerations/trade-action.model';
import { TradeStatus } from 'app/entities/enumerations/trade-status.model';
import { TradeResult } from 'app/entities/enumerations/trade-result.model';
import { TradeType } from 'app/entities/enumerations/trade-type.model';

export interface ITradeSuggestion {
  id?: number;
  action?: TradeAction | null;
  tradeInPrice?: number | null;
  minTradeOutPrice?: number | null;
  minProfitPoints?: number | null;
  betterTradeoutPrice?: number | null;
  betterTradeOutProfitPoints?: number | null;
  actualTradeoutPrice?: number | null;
  actualProfitPoints?: number | null;
  slPoints?: number | null;
  tradeStatus?: TradeStatus | null;
  tradeResults?: TradeResult | null;
  tradeInTime?: dayjs.Dayjs | null;
  tradeOutTime?: dayjs.Dayjs | null;
  tradeDuration?: number | null;
  tradeDate?: dayjs.Dayjs | null;
  tradeSuggestionTime?: dayjs.Dayjs | null;
  tradeType?: TradeType | null;
  actualPL?: number | null;
  slPrice?: number | null;
  currentMarketPrice?: number | null;
}

export class TradeSuggestion implements ITradeSuggestion {
  constructor(
    public id?: number,
    public action?: TradeAction | null,
    public tradeInPrice?: number | null,
    public minTradeOutPrice?: number | null,
    public minProfitPoints?: number | null,
    public betterTradeoutPrice?: number | null,
    public betterTradeOutProfitPoints?: number | null,
    public actualTradeoutPrice?: number | null,
    public actualProfitPoints?: number | null,
    public slPoints?: number | null,
    public tradeStatus?: TradeStatus | null,
    public tradeResults?: TradeResult | null,
    public tradeInTime?: dayjs.Dayjs | null,
    public tradeOutTime?: dayjs.Dayjs | null,
    public tradeDuration?: number | null,
    public tradeDate?: dayjs.Dayjs | null,
    public tradeSuggestionTime?: dayjs.Dayjs | null,
    public tradeType?: TradeType | null,
    public actualPL?: number | null,
    public slPrice?: number | null,
    public currentMarketPrice?: number | null
  ) {}
}

export function getTradeSuggestionIdentifier(tradeSuggestion: ITradeSuggestion): number | undefined {
  return tradeSuggestion.id;
}
