import * as dayjs from 'dayjs';

export interface IGratitude {
  id?: number;
  createdDate?: dayjs.Dayjs;
  loved?: boolean | null;
  achieved?: boolean | null;
  gratefulNote?: string;
}

export class Gratitude implements IGratitude {
  constructor(
    public id?: number,
    public createdDate?: dayjs.Dayjs,
    public loved?: boolean | null,
    public achieved?: boolean | null,
    public gratefulNote?: string
  ) {
    this.loved = this.loved ?? false;
    this.achieved = this.achieved ?? false;
  }
}

export function getGratitudeIdentifier(gratitude: IGratitude): number | undefined {
  return gratitude.id;
}
