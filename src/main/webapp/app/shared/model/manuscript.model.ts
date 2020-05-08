import { Moment } from 'moment';
import { IPayment } from 'app/shared/model/payment.model';
import { IJournal } from 'app/shared/model/journal.model';
import { IAuthor } from 'app/shared/model/author.model';
import { APCStatus } from 'app/shared/model/enumerations/apc-status.model';

export interface IManuscript {
  id?: number;
  manuscriptId?: string;
  manuscriptTitle?: string;
  apcStatus?: APCStatus;
  dateCreated?: Moment;
  dateModified?: Moment;
  linkedPayment?: IPayment;
  manuscriptJournalAcronym?: IJournal;
  manuscriptAuthorName?: IAuthor;
}

export class Manuscript implements IManuscript {
  constructor(
    public id?: number,
    public manuscriptId?: string,
    public manuscriptTitle?: string,
    public apcStatus?: APCStatus,
    public dateCreated?: Moment,
    public dateModified?: Moment,
    public linkedPayment?: IPayment,
    public manuscriptJournalAcronym?: IJournal,
    public manuscriptAuthorName?: IAuthor
  ) {}
}
