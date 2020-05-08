import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { MpsapcSharedModule } from 'app/shared/shared.module';
import { ManuscriptComponent } from './manuscript.component';
import { ManuscriptDetailComponent } from './manuscript-detail.component';
import { ManuscriptUpdateComponent } from './manuscript-update.component';
import { ManuscriptDeleteDialogComponent } from './manuscript-delete-dialog.component';
import { manuscriptRoute } from './manuscript.route';

@NgModule({
  imports: [MpsapcSharedModule, RouterModule.forChild(manuscriptRoute)],
  declarations: [ManuscriptComponent, ManuscriptDetailComponent, ManuscriptUpdateComponent, ManuscriptDeleteDialogComponent],
  entryComponents: [ManuscriptDeleteDialogComponent]
})
export class MpsapcManuscriptModule {}
