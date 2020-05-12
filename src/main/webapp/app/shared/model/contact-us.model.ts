import { Moment } from 'moment';

export interface IContactUs {
  id?: number;
  name?: string;
  email?: string;
  contact?: number;
  message?: string;
  dateAdded?: Moment;
  dateModified?: Moment;
}

export class ContactUs implements IContactUs {
  constructor(
    public id?: number,
    public name?: string,
    public email?: string,
    public contact?: number,
    public message?: string,
    public dateAdded?: Moment,
    public dateModified?: Moment
  ) {}
}
