/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { GesBtpTestModule } from '../../../test.module';
import { ProduitsDetailComponent } from '../../../../../../main/webapp/app/entities/produits/produits-detail.component';
import { ProduitsService } from '../../../../../../main/webapp/app/entities/produits/produits.service';
import { Produits } from '../../../../../../main/webapp/app/entities/produits/produits.model';

describe('Component Tests', () => {

    describe('Produits Management Detail Component', () => {
        let comp: ProduitsDetailComponent;
        let fixture: ComponentFixture<ProduitsDetailComponent>;
        let service: ProduitsService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [GesBtpTestModule],
                declarations: [ProduitsDetailComponent],
                providers: [
                    ProduitsService
                ]
            })
            .overrideTemplate(ProduitsDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ProduitsDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ProduitsService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new Produits(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.produits).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
