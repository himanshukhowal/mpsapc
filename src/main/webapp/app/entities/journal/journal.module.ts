import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { MpsapcSharedModule } from 'app/shared/shared.module';
import { JournalComponent } from './journal.component';
import { JournalDetailComponent } from './journal-detail.component';
import { JournalUpdateComponent } from './journal-update.component';
import { JournalDeleteDialogComponent } from './journal-delete-dialog.component';
import { journalRoute } from './journal.route';

@NgModule({
  imports: [MpsapcSharedModule, RouterModule.forChild(journalRoute)],
  declarations: [JournalComponent, JournalDetailComponent, JournalUpdateComponent, JournalDeleteDialogComponent],
  entryComponents: [JournalDeleteDialogComponent]
})
export class MpsapcJournalModule {}
