import { Moment } from 'moment';
import { IManuscript } from 'app/shared/model/manuscript.model';
import { ActiveStatus } from 'app/shared/model/enumerations/active-status.model';

export interface IJournal {
  id?: number;
  journalAcronym?: string;
  journalTitle?: string;
  activeStatus?: ActiveStatus;
  dateCreated?: Moment;
  dateModified?: Moment;
  journalAcronyms?: IManuscript[];
}

export class Journal implements IJournal {
  constructor(
    public id?: number,
    public journalAcronym?: string,
    public journalTitle?: string,
    public activeStatus?: ActiveStatus,
    public dateCreated?: Moment,
    public dateModified?: Moment,
    public journalAcronyms?: IManuscript[]
  ) {}
}