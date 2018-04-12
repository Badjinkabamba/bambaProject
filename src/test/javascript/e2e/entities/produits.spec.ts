import { browser, element, by } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';

describe('Produits e2e test', () => {

    let navBarPage: NavBarPage;
    let produitsDialogPage: ProduitsDialogPage;
    let produitsComponentsPage: ProduitsComponentsPage;

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load Produits', () => {
        navBarPage.goToEntity('produits');
        produitsComponentsPage = new ProduitsComponentsPage();
        expect(produitsComponentsPage.getTitle())
            .toMatch(/gesBtpApp.produits.home.title/);

    });

    it('should load create Produits dialog', () => {
        produitsComponentsPage.clickOnCreateButton();
        produitsDialogPage = new ProduitsDialogPage();
        expect(produitsDialogPage.getModalTitle())
            .toMatch(/gesBtpApp.produits.home.createOrEditLabel/);
        produitsDialogPage.close();
    });

    it('should create and save Produits', () => {
        produitsComponentsPage.clickOnCreateButton();
        produitsDialogPage.setNomProduitInput('nomProduit');
        expect(produitsDialogPage.getNomProduitInput()).toMatch('nomProduit');
        produitsDialogPage.setPrixUnitaireInput('5');
        expect(produitsDialogPage.getPrixUnitaireInput()).toMatch('5');
        produitsDialogPage.setDescriptionInput('description');
        expect(produitsDialogPage.getDescriptionInput()).toMatch('description');
        produitsDialogPage.userSelectLastOption();
        produitsDialogPage.save();
        expect(produitsDialogPage.getSaveButton().isPresent()).toBeFalsy();
    });

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class ProduitsComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-produits div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class ProduitsDialogPage {
    modalTitle = element(by.css('h4#myProduitsLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    nomProduitInput = element(by.css('input#field_nomProduit'));
    prixUnitaireInput = element(by.css('input#field_prixUnitaire'));
    descriptionInput = element(by.css('input#field_description'));
    userSelect = element(by.css('select#field_user'));

    getModalTitle() {
        return this.modalTitle.getAttribute('jhiTranslate');
    }

    setNomProduitInput = function(nomProduit) {
        this.nomProduitInput.sendKeys(nomProduit);
    };

    getNomProduitInput = function() {
        return this.nomProduitInput.getAttribute('value');
    };

    setPrixUnitaireInput = function(prixUnitaire) {
        this.prixUnitaireInput.sendKeys(prixUnitaire);
    };

    getPrixUnitaireInput = function() {
        return this.prixUnitaireInput.getAttribute('value');
    };

    setDescriptionInput = function(description) {
        this.descriptionInput.sendKeys(description);
    };

    getDescriptionInput = function() {
        return this.descriptionInput.getAttribute('value');
    };

    userSelectLastOption = function() {
        this.userSelect.all(by.tagName('option')).last().click();
    };

    userSelectOption = function(option) {
        this.userSelect.sendKeys(option);
    };

    getUserSelect = function() {
        return this.userSelect;
    };

    getUserSelectedOption = function() {
        return this.userSelect.element(by.css('option:checked')).getText();
    };

    save() {
        this.saveButton.click();
    }

    close() {
        this.closeButton.click();
    }

    getSaveButton() {
        return this.saveButton;
    }
}
