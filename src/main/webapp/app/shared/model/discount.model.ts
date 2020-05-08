import { Moment } from 'moment';
import { DiscountType } from 'app/shared/model/enumerations/discount-type.model';
import { ActiveStatus } from 'app/shared/model/enumerations/active-status.model';

export interface IDiscount {
  id?: number;
  discountType?: DiscountType;
  entityName?: string;
  amount?: number;
  activeStatus?: ActiveStatus;
  dateCreated?: Moment;
  dateModified?: Moment;
}

export class Discount implements IDiscount {
  constructor(
    public id?: number,
    public discountType?: DiscountType,
    public entityName?: string,
    public amount?: number,
    public activeStatus?: ActiveStatus,
    public dateCreated?: Moment,
    public dateModified?: Moment
  ) {}
}