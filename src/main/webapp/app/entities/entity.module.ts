import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'journal',
        loadChildren: () => import('./journal/journal.module').then(m => m.MpsapcJournalModule)
      },
      {
        path: 'manuscript',
        loadChildren: () => import('./manuscript/manuscript.module').then(m => m.MpsapcManuscriptModule)
      },
      {
        path: 'waiver',
        loadChildren: () => import('./waiver/waiver.module').then(m => m.MpsapcWaiverModule)
      },
      {
        path: 'discount',
        loadChildren: () => import('./discount/discount.module').then(m => m.MpsapcDiscountModule)
      },
      {
        path: 'contact-us',
        loadChildren: () => import('./contact-us/contact-us.module').then(m => m.MpsapcContactUsModule)
      },
      {
        path: 'author',
        loadChildren: () => import('./author/author.module').then(m => m.MpsapcAuthorModule)
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ]
})
export class MpsapcEntityModule {}
