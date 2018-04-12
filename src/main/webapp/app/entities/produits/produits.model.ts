import { BaseEntity, User } from './../../shared';

export class Produits implements BaseEntity {
    constructor(
        public id?: number,
        public nomProduit?: string,
        public prixUnitaire?: number,
        public description?: string,
        public user?: User,
    ) {
    }
}
