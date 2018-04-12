/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { GesBtpTestModule } from '../../../test.module';
import { ProduitsComponent } from '../../../../../../main/webapp/app/entities/produits/produits.component';
import { ProduitsService } from '../../../../../../main/webapp/app/entities/produits/produits.service';
import { Produits } from '../../../../../../main/webapp/app/entities/produits/produits.model';

describe('Component Tests', () => {

    describe('Produits Management Component', () => {
        let comp: ProduitsComponent;
        let fixture: ComponentFixture<ProduitsComponent>;
        let service: ProduitsService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [GesBtpTestModule],
                declarations: [ProduitsComponent],
                providers: [
                    ProduitsService
                ]
            })
            .overrideTemplate(ProduitsComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ProduitsComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ProduitsService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new Produits(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.produits[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
