import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { Produits } from './produits.model';
import { ProduitsService } from './produits.service';

@Component({
    selector: 'jhi-produits-detail',
    templateUrl: './produits-detail.component.html'
})
export class ProduitsDetailComponent implements OnInit, OnDestroy {

    produits: Produits;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private produitsService: ProduitsService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInProduits();
    }

    load(id) {
        this.produitsService.find(id)
            .subscribe((produitsResponse: HttpResponse<Produits>) => {
                this.produits = produitsResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInProduits() {
        this.eventSubscriber = this.eventManager.subscribe(
            'produitsListModification',
            (response) => this.load(this.produits.id)
        );
    }
}
