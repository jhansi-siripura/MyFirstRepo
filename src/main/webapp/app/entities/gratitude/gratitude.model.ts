import * as dayjs from 'dayjs';

export interface IGratitude {
  id?: number;
  gratefulNote?: string;
  createdDate?: dayjs.Dayjs;
  loved?: boolean | null;
  achieved?: boolean | null;
}

export class Gratitude implements IGratitude {
  constructor(
    public id?: number,
    public gratefulNote?: string,
    public createdDate?: dayjs.Dayjs,
    public loved?: boolean | null,
    public achieved?: boolean | null
  ) {
    this.loved = this.loved ?? false;
    this.achieved = this.achieved ?? false;
  }
}

export function getGratitudeIdentifier(gratitude: IGratitude): number | undefined {
  return gratitude.id;
}
