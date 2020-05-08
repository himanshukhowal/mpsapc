import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { MpsapcSharedModule } from 'app/shared/shared.module';
import { MailTemplatesComponent } from './mail-templates.component';
import { MailTemplatesDetailComponent } from './mail-templates-detail.component';
import { MailTemplatesUpdateComponent } from './mail-templates-update.component';
import { MailTemplatesDeleteDialogComponent } from './mail-templates-delete-dialog.component';
import { mailTemplatesRoute } from './mail-templates.route';

@NgModule({
  imports: [MpsapcSharedModule, RouterModule.forChild(mailTemplatesRoute)],
  declarations: [MailTemplatesComponent, MailTemplatesDetailComponent, MailTemplatesUpdateComponent, MailTemplatesDeleteDialogComponent],
  entryComponents: [MailTemplatesDeleteDialogComponent]
})
export class MpsapcMailTemplatesModule {}
