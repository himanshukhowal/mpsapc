import { Moment } from 'moment';
import { WaiverType } from 'app/shared/model/enumerations/waiver-type.model';
import { ActiveStatus } from 'app/shared/model/enumerations/active-status.model';

export interface IWaiver {
  id?: number;
  waiverType?: WaiverType;
  entityName?: string;
  activeStatus?: ActiveStatus;
  dateCreated?: Moment;
  dateModified?: Moment;
}

export class Waiver implements IWaiver {
  constructor(
    public id?: number,
    public waiverType?: WaiverType,
    public entityName?: string,
    public activeStatus?: ActiveStatus,
    public dateCreated?: Moment,
    public dateModified?: Moment
  ) {}
}
