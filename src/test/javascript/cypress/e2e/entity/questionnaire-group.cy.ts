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

describe('QuestionnaireGroup e2e test', () => {
  const questionnaireGroupPageUrl = '/questionnaire-group';
  const questionnaireGroupPageUrlPattern = new RegExp('/questionnaire-group(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const questionnaireGroupSample = {};

  let questionnaireGroup;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/questionnaire-groups+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/questionnaire-groups').as('postEntityRequest');
    cy.intercept('DELETE', '/api/questionnaire-groups/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (questionnaireGroup) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/questionnaire-groups/${questionnaireGroup.id}`,
      }).then(() => {
        questionnaireGroup = undefined;
      });
    }
  });

  it('QuestionnaireGroups menu should load QuestionnaireGroups page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('questionnaire-group');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('QuestionnaireGroup').should('exist');
    cy.url().should('match', questionnaireGroupPageUrlPattern);
  });

  describe('QuestionnaireGroup page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(questionnaireGroupPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create QuestionnaireGroup page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/questionnaire-group/new$'));
        cy.getEntityCreateUpdateHeading('QuestionnaireGroup');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', questionnaireGroupPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/questionnaire-groups',
          body: questionnaireGroupSample,
        }).then(({ body }) => {
          questionnaireGroup = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/questionnaire-groups+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [questionnaireGroup],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(questionnaireGroupPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details QuestionnaireGroup page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('questionnaireGroup');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', questionnaireGroupPageUrlPattern);
      });

      it('edit button click should load edit QuestionnaireGroup page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('QuestionnaireGroup');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', questionnaireGroupPageUrlPattern);
      });

      it('edit button click should load edit QuestionnaireGroup page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('QuestionnaireGroup');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', questionnaireGroupPageUrlPattern);
      });

      it('last delete button click should delete instance of QuestionnaireGroup', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('questionnaireGroup').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', questionnaireGroupPageUrlPattern);

        questionnaireGroup = undefined;
      });
    });
  });

  describe('new QuestionnaireGroup page', () => {
    beforeEach(() => {
      cy.visit(`${questionnaireGroupPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('QuestionnaireGroup');
    });

    it('should create an instance of QuestionnaireGroup', () => {
      cy.get(`[data-cy="name"]`).type('open-source Handmade').should('have.value', 'open-source Handmade');

      cy.get(`[data-cy="description"]`).type('quantifying').should('have.value', 'quantifying');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        questionnaireGroup = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', questionnaireGroupPageUrlPattern);
    });
  });
});
