import {
  entityTableSelector,
  entityDetailsButtonSelector,
  entityDetailsBackButtonSelector,
  entityCreateButtonSelector,
  entityCreateSaveButtonSelector,
  entityCreateCancelButtonSelector,
  entityEditButtonSelector,
  entityDeleteButtonSelector,
  entityConfirmDeleteButtonSelector,
} from '../../support/entity';

describe('TestEntity e2e test', () => {
  const testEntityPageUrl = '/test-entity';
  const testEntityPageUrlPattern = new RegExp('/test-entity(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const testEntitySample = {};

  let testEntity;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/test-entities+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/test-entities').as('postEntityRequest');
    cy.intercept('DELETE', '/api/test-entities/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (testEntity) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/test-entities/${testEntity.id}`,
      }).then(() => {
        testEntity = undefined;
      });
    }
  });

  it('TestEntities menu should load TestEntities page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('test-entity');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('TestEntity').should('exist');
    cy.url().should('match', testEntityPageUrlPattern);
  });

  describe('TestEntity page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(testEntityPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create TestEntity page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/test-entity/new$'));
        cy.getEntityCreateUpdateHeading('TestEntity');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', testEntityPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/test-entities',
          body: testEntitySample,
        }).then(({ body }) => {
          testEntity = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/test-entities+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [testEntity],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(testEntityPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details TestEntity page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('testEntity');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', testEntityPageUrlPattern);
      });

      it('edit button click should load edit TestEntity page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('TestEntity');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', testEntityPageUrlPattern);
      });

      it.skip('edit button click should load edit TestEntity page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('TestEntity');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', testEntityPageUrlPattern);
      });

      it('last delete button click should delete instance of TestEntity', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('testEntity').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', testEntityPageUrlPattern);

        testEntity = undefined;
      });
    });
  });

  describe('new TestEntity page', () => {
    beforeEach(() => {
      cy.visit(`${testEntityPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('TestEntity');
    });

    it('should create an instance of TestEntity', () => {
      cy.get(`[data-cy="stringField"]`).type('Capacità primary Buckinghamshire').should('have.value', 'Capacità primary Buckinghamshire');

      cy.get(`[data-cy="integerField"]`).type('97617').should('have.value', '97617');

      cy.get(`[data-cy="longField"]`).type('62345').should('have.value', '62345');

      cy.get(`[data-cy="bigDecimalField"]`).type('42031').should('have.value', '42031');

      cy.get(`[data-cy="floatField"]`).type('58727').should('have.value', '58727');

      cy.get(`[data-cy="doubleField"]`).type('91283').should('have.value', '91283');

      cy.get(`[data-cy="enumField"]`).select('INTEGER');

      cy.get(`[data-cy="booleanField"]`).should('not.be.checked');
      cy.get(`[data-cy="booleanField"]`).click().should('be.checked');

      cy.get(`[data-cy="localDateField"]`).type('2023-03-03').blur().should('have.value', '2023-03-03');

      cy.get(`[data-cy="zonedDateField"]`).type('2023-03-03T06:11').blur().should('have.value', '2023-03-03T06:11');

      cy.get(`[data-cy="instantField"]`).type('2023-03-03T07:24').blur().should('have.value', '2023-03-03T07:24');

      cy.get(`[data-cy="durationField"]`).type('PT23M').blur().should('have.value', 'PT23M');

      cy.get(`[data-cy="uuidField"]`)
        .type('7352a24d-4dc7-4a79-9c3b-c638b19e5ba5')
        .invoke('val')
        .should('match', new RegExp('7352a24d-4dc7-4a79-9c3b-c638b19e5ba5'));

      cy.setFieldImageAsBytesOfEntity('blobField', 'integration-test.png', 'image/png');

      cy.setFieldImageAsBytesOfEntity('anyBlobField', 'integration-test.png', 'image/png');

      cy.setFieldImageAsBytesOfEntity('imageBlobField', 'integration-test.png', 'image/png');

      cy.get(`[data-cy="textBlobField"]`)
        .type('../fake-data/blob/hipster.txt')
        .invoke('val')
        .should('match', new RegExp('../fake-data/blob/hipster.txt'));

      // since cypress clicks submit too fast before the blob fields are validated
      cy.wait(200); // eslint-disable-line cypress/no-unnecessary-waiting
      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        testEntity = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', testEntityPageUrlPattern);
    });
  });
});
