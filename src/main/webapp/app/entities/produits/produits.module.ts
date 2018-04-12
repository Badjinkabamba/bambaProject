import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { GesBtpSharedModule } from '../../shared';
import { GesBtpAdminModule } from '../../admin/admin.module';
import {
    ProduitsService,
    ProduitsPopupService,
    ProduitsComponent,
    ProduitsDetailComponent,
    ProduitsDialogComponent,
    ProduitsPopupComponent,
    ProduitsDeletePopupComponent,
    ProduitsDeleteDialogComponent,
    produitsRoute,
    produitsPopupRoute,
} from './';

const ENTITY_STATES = [
    ...produitsRoute,
    ...produitsPopupRoute,
];

@NgModule({
    imports: [
        GesBtpSharedModule,
        GesBtpAdminModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        ProduitsComponent,
        ProduitsDetailComponent,
        ProduitsDialogComponent,
        ProduitsDeleteDialogComponent,
        ProduitsPopupComponent,
        ProduitsDeletePopupComponent,
    ],
    entryComponents: [
        ProduitsComponent,
        ProduitsDialogComponent,
        ProduitsPopupComponent,
        ProduitsDeleteDialogComponent,
        ProduitsDeletePopupComponent,
    ],
    providers: [
        ProduitsService,
        ProduitsPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class GesBtpProduitsModule {}
