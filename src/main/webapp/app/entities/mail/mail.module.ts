import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { MpsapcSharedModule } from 'app/shared/shared.module';
import { MailComponent } from './mail.component';
import { MailDetailComponent } from './mail-detail.component';
import { MailUpdateComponent } from './mail-update.component';
import { MailDeleteDialogComponent } from './mail-delete-dialog.component';
import { mailRoute } from './mail.route';

@NgModule({
  imports: [MpsapcSharedModule, RouterModule.forChild(mailRoute)],
  declarations: [MailComponent, MailDetailComponent, MailUpdateComponent, MailDeleteDialogComponent],
  entryComponents: [MailDeleteDialogComponent]
})
export class MpsapcMailModule {}
