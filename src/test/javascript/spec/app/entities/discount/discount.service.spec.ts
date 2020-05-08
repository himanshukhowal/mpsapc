import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { DiscountService } from 'app/entities/discount/discount.service';
import { IDiscount, Discount } from 'app/shared/model/discount.model';
import { DiscountType } from 'app/shared/model/enumerations/discount-type.model';
import { ActiveStatus } from 'app/shared/model/enumerations/active-status.model';

describe('Service Tests', () => {
  describe('Discount Service', () => {
    let injector: TestBed;
    let service: DiscountService;
    let httpMock: HttpTestingController;
    let elemDefault: IDiscount;
    let expectedResult: IDiscount | IDiscount[] | boolean | null;
    let currentDate: moment.Moment;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule]
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(DiscountService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new Discount(0, DiscountType.Journal, 'AAAAAAA', 0, ActiveStatus.Active, currentDate, currentDate);
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

      it('should create a Discount', () => {
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

        service.create(new Discount()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Discount', () => {
        const returnedFromService = Object.assign(
          {
            discountType: 'BBBBBB',
            entityName: 'BBBBBB',
            amount: 1,
            activeStatus: 'BBBBBB',
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

      it('should return a list of Discount', () => {
        const returnedFromService = Object.assign(
          {
            discountType: 'BBBBBB',
            entityName: 'BBBBBB',
            amount: 1,
            activeStatus: 'BBBBBB',
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

      it('should delete a Discount', () => {
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
