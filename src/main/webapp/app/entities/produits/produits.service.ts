import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { Produits } from './produits.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<Produits>;

@Injectable()
export class ProduitsService {

    private resourceUrl =  SERVER_API_URL + 'api/produits';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/produits';

    constructor(private http: HttpClient) { }

    create(produits: Produits): Observable<EntityResponseType> {
        const copy = this.convert(produits);
        return this.http.post<Produits>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(produits: Produits): Observable<EntityResponseType> {
        const copy = this.convert(produits);
        return this.http.put<Produits>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<Produits>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<Produits[]>> {
        const options = createRequestOption(req);
        return this.http.get<Produits[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Produits[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    search(req?: any): Observable<HttpResponse<Produits[]>> {
        const options = createRequestOption(req);
        return this.http.get<Produits[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Produits[]>) => this.convertArrayResponse(res));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: Produits = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<Produits[]>): HttpResponse<Produits[]> {
        const jsonResponse: Produits[] = res.body;
        const body: Produits[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to Produits.
     */
    private convertItemFromServer(produits: Produits): Produits {
        const copy: Produits = Object.assign({}, produits);
        return copy;
    }

    /**
     * Convert a Produits to a JSON which can be sent to the server.
     */
    private convert(produits: Produits): Produits {
        const copy: Produits = Object.assign({}, produits);
        return copy;
    }
}
