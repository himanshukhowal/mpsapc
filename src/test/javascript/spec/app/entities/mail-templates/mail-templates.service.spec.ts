import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { MailTemplatesService } from 'app/entities/mail-templates/mail-templates.service';
import { IMailTemplates, MailTemplates } from 'app/shared/model/mail-templates.model';

describe('Service Tests', () => {
  describe('MailTemplates Service', () => {
    let injector: TestBed;
    let service: MailTemplatesService;
    let httpMock: HttpTestingController;
    let elemDefault: IMailTemplates;
    let expectedResult: IMailTemplates | IMailTemplates[] | boolean | null;
    let currentDate: moment.Moment;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule]
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(MailTemplatesService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new MailTemplates(0, 'AAAAAAA', 'AAAAAAA', 'AAAAAAA', 'AAAAAAA', 'AAAAAAA', currentDate, currentDate);
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            dateCreated: currentDate.format(DATE_FORMAT),
            dateModified: currentDate.format(DATE_FORMAT)
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a MailTemplates', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            dateCreated: currentDate.format(DATE_FORMAT),
            dateModified: currentDate.format(DATE_FORMAT)
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateCreated: currentDate,
            dateModified: currentDate
          },
          returnedFromService
        );

        service.create(new MailTemplates()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a MailTemplates', () => {
        const returnedFromService = Object.assign(
          {
            templateName: 'BBBBBB',
            mailCC: 'BBBBBB',
            mailBCC: 'BBBBBB',
            mailSubject: 'BBBBBB',
            mailBody: 'BBBBBB',
            dateCreated: currentDate.format(DATE_FORMAT),
            dateModified: currentDate.format(DATE_FORMAT)
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateCreated: currentDate,
            dateModified: currentDate
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of MailTemplates', () => {
        const returnedFromService = Object.assign(
          {
            templateName: 'BBBBBB',
            mailCC: 'BBBBBB',
            mailBCC: 'BBBBBB',
            mailSubject: 'BBBBBB',
            mailBody: 'BBBBBB',
            dateCreated: currentDate.format(DATE_FORMAT),
            dateModified: currentDate.format(DATE_FORMAT)
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateCreated: currentDate,
            dateModified: currentDate
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a MailTemplates', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
