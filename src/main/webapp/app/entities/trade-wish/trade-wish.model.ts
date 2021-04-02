import * as dayjs from 'dayjs';

export interface ITradeWish {
  id?: number;
  twish?: number;
  picked?: boolean | null;
  pickedDate?: dayjs.Dayjs;
}

export class TradeWish implements ITradeWish {
  constructor(public id?: number, public twish?: number, public picked?: boolean | null, public pickedDate?: dayjs.Dayjs) {
    this.picked = this.picked ?? false;
  }
}

export function getTradeWishIdentifier(tradeWish: ITradeWish): number | undefined {
  return tradeWish.id;
}
