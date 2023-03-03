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

describe('Questionnaire e2e test', () => {
  const questionnairePageUrl = '/questionnaire';
  const questionnairePageUrlPattern = new RegExp('/questionnaire(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const questionnaireSample = {};

  let questionnaire;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/questionnaires+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/questionnaires').as('postEntityRequest');
    cy.intercept('DELETE', '/api/questionnaires/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (questionnaire) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/questionnaires/${questionnaire.id}`,
      }).then(() => {
        questionnaire = undefined;
      });
    }
  });

  it('Questionnaires menu should load Questionnaires page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('questionnaire');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Questionnaire').should('exist');
    cy.url().should('match', questionnairePageUrlPattern);
  });

  describe('Questionnaire page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(questionnairePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Questionnaire page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/questionnaire/new$'));
        cy.getEntityCreateUpdateHeading('Questionnaire');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', questionnairePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/questionnaires',
          body: questionnaireSample,
        }).then(({ body }) => {
          questionnaire = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/questionnaires+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [questionnaire],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(questionnairePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Questionnaire page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('questionnaire');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', questionnairePageUrlPattern);
      });

      it('edit button click should load edit Questionnaire page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Questionnaire');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', questionnairePageUrlPattern);
      });

      it.skip('edit button click should load edit Questionnaire page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Questionnaire');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', questionnairePageUrlPattern);
      });

      it('last delete button click should delete instance of Questionnaire', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('questionnaire').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', questionnairePageUrlPattern);

        questionnaire = undefined;
      });
    });
  });

  describe('new Questionnaire page', () => {
    beforeEach(() => {
      cy.visit(`${questionnairePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Questionnaire');
    });

    it('should create an instance of Questionnaire', () => {
      cy.get(`[data-cy="name"]`).type('back-end spedizioni Dynamic').should('have.value', 'back-end spedizioni Dynamic');

      cy.get(`[data-cy="version"]`).type('monitorata').should('have.value', 'monitorata');

      cy.get(`[data-cy="title"]`).type('e-commerce Outdoors').should('have.value', 'e-commerce Outdoors');

      cy.get(`[data-cy="subTitle"]`).type('Contingenza').should('have.value', 'Contingenza');

      cy.get(`[data-cy="notes"]`).type('Bike').should('have.value', 'Bike');

      cy.get(`[data-cy="image"]`).type('Card sinergica Garden').should('have.value', 'Card sinergica Garden');

      cy.get(`[data-cy="imageAlt"]`).type('Handmade Sinergia').should('have.value', 'Handmade Sinergia');

      cy.get(`[data-cy="instructions"]`).type('enterprise Concrete scalabile').should('have.value', 'enterprise Concrete scalabile');

      cy.get(`[data-cy="compilationTime"]`).type('4804').should('have.value', '4804');

      cy.get(`[data-cy="forcedTerminationTime"]`).type('65187').should('have.value', '65187');

      cy.get(`[data-cy="usedSeconds"]`).type('52455').should('have.value', '52455');

      cy.get(`[data-cy="status"]`).type('63432').should('have.value', '63432');

      cy.get(`[data-cy="xml"]`).type('estensibili innovativi').should('have.value', 'estensibili innovativi');

      cy.get(`[data-cy="json"]`).type('Frozen Computer invoice').should('have.value', 'Frozen Computer invoice');

      cy.get(`[data-cy="saveText"]`).type('mobile innovazione').should('have.value', 'mobile innovazione');

      cy.get(`[data-cy="searchText"]`).type('Awesome card').should('have.value', 'Awesome card');

      cy.get(`[data-cy="subjectToEvaluation"]`).type('withdrawal Strategist Global').should('have.value', 'withdrawal Strategist Global');

      cy.get(`[data-cy="questionnaireType"]`).select('AUTOVALUTAZIONI');

      cy.get(`[data-cy="attachments"]`).type('71599').should('have.value', '71599');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        questionnaire = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', questionnairePageUrlPattern);
    });
  });
});
