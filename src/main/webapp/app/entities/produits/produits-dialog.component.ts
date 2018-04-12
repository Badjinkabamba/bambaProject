import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Produits } from './produits.model';
import { ProduitsPopupService } from './produits-popup.service';
import { ProduitsService } from './produits.service';
import { User, UserService } from '../../shared';

@Component({
    selector: 'jhi-produits-dialog',
    templateUrl: './produits-dialog.component.html'
})
export class ProduitsDialogComponent implements OnInit {

    produits: Produits;
    isSaving: boolean;

    users: User[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private produitsService: ProduitsService,
        private userService: UserService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.userService.query()
            .subscribe((res: HttpResponse<User[]>) => { this.users = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.produits.id !== undefined) {
            this.subscribeToSaveResponse(
                this.produitsService.update(this.produits));
        } else {
            this.subscribeToSaveResponse(
                this.produitsService.create(this.produits));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<Produits>>) {
        result.subscribe((res: HttpResponse<Produits>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: Produits) {
        this.eventManager.broadcast({ name: 'produitsListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackUserById(index: number, item: User) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-produits-popup',
    template: ''
})
export class ProduitsPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private produitsPopupService: ProduitsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.produitsPopupService
                    .open(ProduitsDialogComponent as Component, params['id']);
            } else {
                this.produitsPopupService
                    .open(ProduitsDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
