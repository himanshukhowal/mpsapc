import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMailTemplates } from 'app/shared/model/mail-templates.model';

@Component({
  selector: 'jhi-mail-templates-detail',
  templateUrl: './mail-templates-detail.component.html'
})
export class MailTemplatesDetailComponent implements OnInit {
  mailTemplates: IMailTemplates | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ mailTemplates }) => (this.mailTemplates = mailTemplates));
  }

  previousState(): void {
    window.history.back();
  }
}
