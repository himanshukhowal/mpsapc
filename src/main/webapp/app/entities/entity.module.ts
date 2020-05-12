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
        path: 'author',
        loadChildren: () => import('./author/author.module').then(m => m.MpsapcAuthorModule)
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
        path: 'payment',
        loadChildren: () => import('./payment/payment.module').then(m => m.MpsapcPaymentModule)
      },
      {
        path: 'mail',
        loadChildren: () => import('./mail/mail.module').then(m => m.MpsapcMailModule)
      },
      {
        path: 'mail-templates',
        loadChildren: () => import('./mail-templates/mail-templates.module').then(m => m.MpsapcMailTemplatesModule)
      },
      {
        path: 'contact-us',
        loadChildren: () => import('./contact-us/contact-us.module').then(m => m.MpsapcContactUsModule)
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ]
})
export class MpsapcEntityModule {}
