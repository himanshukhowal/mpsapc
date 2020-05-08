import { Moment } from 'moment';
import { IMail } from 'app/shared/model/mail.model';

export interface IMailTemplates {
  id?: number;
  templateName?: string;
  mailCC?: string;
  mailBCC?: string;
  mailSubject?: string;
  mailBody?: string;
  dateCreated?: Moment;
  dateModified?: Moment;
  templateNames?: IMail[];
}

export class MailTemplates implements IMailTemplates {
  constructor(
    public id?: number,
    public templateName?: string,
    public mailCC?: string,
    public mailBCC?: string,
    public mailSubject?: string,
    public mailBody?: string,
    public dateCreated?: Moment,
    public dateModified?: Moment,
    public templateNames?: IMail[]
  ) {}
}
