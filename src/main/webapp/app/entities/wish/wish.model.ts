import * as dayjs from 'dayjs';
import { WishCategory } from 'app/entities/enumerations/wish-category.model';

export interface IWish {
  id?: number;
  myWishNote?: string;
  startDate?: dayjs.Dayjs | null;
  fulfilled?: boolean | null;
  fulfilledDate?: dayjs.Dayjs | null;
  duration?: number | null;
  category?: WishCategory | null;
}

export class Wish implements IWish {
  constructor(
    public id?: number,
    public myWishNote?: string,
    public startDate?: dayjs.Dayjs | null,
    public fulfilled?: boolean | null,
    public fulfilledDate?: dayjs.Dayjs | null,
    public duration?: number | null,
    public category?: WishCategory | null
  ) {
    this.fulfilled = this.fulfilled ?? false;
  }
}

export function getWishIdentifier(wish: IWish): number | undefined {
  return wish.id;
}
