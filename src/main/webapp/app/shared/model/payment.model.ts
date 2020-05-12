import { Moment } from 'moment';

export interface IPayment {
  id?: number;
  currency?: string;
  amount?: number;
  dateCreated?: Moment;
  dateModified?: Moment;
}

export class Payment implements IPayment {
  constructor(
    public id?: number,
    public currency?: string,
    public amount?: number,
    public dateCreated?: Moment,
    public dateModified?: Moment
  ) {}
}
