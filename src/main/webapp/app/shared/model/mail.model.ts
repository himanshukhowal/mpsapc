import { Moment } from 'moment';
import { IJournal } from 'app/shared/model/journal.model';
import { IMailTemplates } from 'app/shared/model/mail-templates.model';
import { APCStatus } from 'app/shared/model/enumerations/apc-status.model';
import { ActiveStatus } from 'app/shared/model/enumerations/active-status.model';

export interface IMail {
  id?: number;
  mailConfigurationName?: string;
  stageName?: APCStatus;
  activeStatus?: ActiveStatus;
  dateCreated?: Moment;
  dateModified?: Moment;
  associatedJournal?: IJournal;
  associatedMailTemplate?: IMailTemplates;
}

export class Mail implements IMail {
  constructor(
    public id?: number,
    public mailConfigurationName?: string,
    public stageName?: APCStatus,
    public activeStatus?: ActiveStatus,
    public dateCreated?: Moment,
    public dateModified?: Moment,
    public associatedJournal?: IJournal,
    public associatedMailTemplate?: IMailTemplates
  ) {}
}
