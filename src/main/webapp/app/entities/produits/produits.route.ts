import { Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { ProduitsComponent } from './produits.component';
import { ProduitsDetailComponent } from './produits-detail.component';
import { ProduitsPopupComponent } from './produits-dialog.component';
import { ProduitsDeletePopupComponent } from './produits-delete-dialog.component';

export const produitsRoute: Routes = [
    {
        path: 'produits',
        component: ProduitsComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gesBtpApp.produits.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'produits/:id',
        component: ProduitsDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gesBtpApp.produits.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const produitsPopupRoute: Routes = [
    {
        path: 'produits-new',
        component: ProduitsPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gesBtpApp.produits.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'produits/:id/edit',
        component: ProduitsPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gesBtpApp.produits.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'produits/:id/delete',
        component: ProduitsDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gesBtpApp.produits.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
