import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { MpsapcSharedModule } from 'app/shared/shared.module';
import { WaiverComponent } from './waiver.component';
import { WaiverDetailComponent } from './waiver-detail.component';
import { WaiverUpdateComponent } from './waiver-update.component';
import { WaiverDeleteDialogComponent } from './waiver-delete-dialog.component';
import { waiverRoute } from './waiver.route';

@NgModule({
  imports: [MpsapcSharedModule, RouterModule.forChild(waiverRoute)],
  declarations: [WaiverComponent, WaiverDetailComponent, WaiverUpdateComponent, WaiverDeleteDialogComponent],
  entryComponents: [WaiverDeleteDialogComponent]
})
export class MpsapcWaiverModule {}
