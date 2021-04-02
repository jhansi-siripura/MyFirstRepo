import * as dayjs from 'dayjs';

export interface ITradeWish {
  id?: number;
  tradeWishNote?: number;
  picked?: boolean | null;
  pickedDate?: dayjs.Dayjs;
}

export class TradeWish implements ITradeWish {
  constructor(public id?: number, public tradeWishNote?: number, public picked?: boolean | null, public pickedDate?: dayjs.Dayjs) {
    this.picked = this.picked ?? false;
  }
}

export function getTradeWishIdentifier(tradeWish: ITradeWish): number | undefined {
  return tradeWish.id;
}
