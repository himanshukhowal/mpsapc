import { IManuscript } from 'app/shared/model/manuscript.model';

export interface IAuthor {
  id?: number;
  firstName?: string;
  middleName?: string;
  lastName?: string;
  email?: string;
  address?: string;
  city?: string;
  state?: string;
  country?: string;
  institute?: string;
  designation?: string;
  firstNames?: IManuscript[];
}

export class Author implements IAuthor {
  constructor(
    public id?: number,
    public firstName?: string,
    public middleName?: string,
    public lastName?: string,
    public email?: string,
    public address?: string,
    public city?: string,
    public state?: string,
    public country?: string,
    public institute?: string,
    public designation?: string,
    public firstNames?: IManuscript[]
  ) {}
}
