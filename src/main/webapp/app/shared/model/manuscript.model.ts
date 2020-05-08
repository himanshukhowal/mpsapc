import { Moment } from 'moment';
import { IJournal } from 'app/shared/model/journal.model';
import { APCStatus } from 'app/shared/model/enumerations/apc-status.model';

export interface IManuscript {
  id?: number;
  manuscriptId?: string;
  manuscriptTitle?: string;
  apcStatus?: APCStatus;
  dateCreated?: Moment;
  dateModified?: Moment;
  manuscriptJournalAcronym?: IJournal;
}

export class Manuscript implements IManuscript {
  constructor(
    public id?: number,
    public manuscriptId?: string,
    public manuscriptTitle?: string,
    public apcStatus?: APCStatus,
    public dateCreated?: Moment,
    public dateModified?: Moment,
    public manuscriptJournalAcronym?: IJournal
  ) {}
}
