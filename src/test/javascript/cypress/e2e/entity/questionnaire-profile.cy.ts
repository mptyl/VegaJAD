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

describe('QuestionnaireProfile e2e test', () => {
  const questionnaireProfilePageUrl = '/questionnaire-profile';
  const questionnaireProfilePageUrlPattern = new RegExp('/questionnaire-profile(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const questionnaireProfileSample = {};

  let questionnaireProfile;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/questionnaire-profiles+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/questionnaire-profiles').as('postEntityRequest');
    cy.intercept('DELETE', '/api/questionnaire-profiles/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (questionnaireProfile) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/questionnaire-profiles/${questionnaireProfile.id}`,
      }).then(() => {
        questionnaireProfile = undefined;
      });
    }
  });

  it('QuestionnaireProfiles menu should load QuestionnaireProfiles page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('questionnaire-profile');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('QuestionnaireProfile').should('exist');
    cy.url().should('match', questionnaireProfilePageUrlPattern);
  });

  describe('QuestionnaireProfile page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(questionnaireProfilePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create QuestionnaireProfile page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/questionnaire-profile/new$'));
        cy.getEntityCreateUpdateHeading('QuestionnaireProfile');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', questionnaireProfilePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/questionnaire-profiles',
          body: questionnaireProfileSample,
        }).then(({ body }) => {
          questionnaireProfile = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/questionnaire-profiles+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [questionnaireProfile],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(questionnaireProfilePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details QuestionnaireProfile page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('questionnaireProfile');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', questionnaireProfilePageUrlPattern);
      });

      it('edit button click should load edit QuestionnaireProfile page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('QuestionnaireProfile');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', questionnaireProfilePageUrlPattern);
      });

      it('edit button click should load edit QuestionnaireProfile page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('QuestionnaireProfile');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', questionnaireProfilePageUrlPattern);
      });

      it('last delete button click should delete instance of QuestionnaireProfile', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('questionnaireProfile').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', questionnaireProfilePageUrlPattern);

        questionnaireProfile = undefined;
      });
    });
  });

  describe('new QuestionnaireProfile page', () => {
    beforeEach(() => {
      cy.visit(`${questionnaireProfilePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('QuestionnaireProfile');
    });

    it('should create an instance of QuestionnaireProfile', () => {
      cy.get(`[data-cy="description"]`).type('Reggio').should('have.value', 'Reggio');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        questionnaireProfile = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', questionnaireProfilePageUrlPattern);
    });
  });
});
