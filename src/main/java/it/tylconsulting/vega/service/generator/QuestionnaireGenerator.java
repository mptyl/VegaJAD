package it.tylconsulting.vega.service.generator;

import com.github.javafaker.Faker;
import it.tylconsulting.vega.domain.Questionnaire;
import it.tylconsulting.vega.domain.QuestionnaireGroup;
import it.tylconsulting.vega.domain.QuestionnaireProfile;
import it.tylconsulting.vega.repository.QuestionnaireGroupRepository;
import it.tylconsulting.vega.repository.QuestionnaireProfileRepository;
import it.tylconsulting.vega.repository.QuestionnaireRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Component
public class QuestionnaireGenerator {

    @Bean
    public CommandLineRunner loadQuestionnaireData(
        QuestionnaireRepository questionnaireRepository,
        QuestionnaireGroupRepository questionnaireGroupRepository,
        QuestionnaireProfileRepository profileRepository)
        {
        return args -> {
            final int NUMBERQUESTTOGENERATE = 1000;
            final int NUMBERQUESTGROUPTOGENERATE = 20;
            final int NUMBERQUESTPROFILETOGENERATE = 10;
            Logger logger = LoggerFactory.getLogger(getClass());


            if (questionnaireRepository.count() != 0L) {
                logger.info("Questionnaire already loaded");
                return;
            }
            questionnaireGroupRepository.deleteAll();
            profileRepository.deleteAll();
            Faker faker = new Faker(Locale.ITALY);

            logger.info("Generating demo data for Questionnaire");


            logger.info("... generating {0} QuestionnaireGroup entities...",NUMBERQUESTGROUPTOGENERATE);
            List<QuestionnaireGroup> questionnaireGroupList = new ArrayList<>();
            for (int i = 1; i <= NUMBERQUESTGROUPTOGENERATE; i++) {
                QuestionnaireGroup questionnaireGroup= new QuestionnaireGroup();
                questionnaireGroup.setId((long) i);
                questionnaireGroup.setName(faker.funnyName().name());
                questionnaireGroup.setDescription(faker.hipster().word());
                questionnaireGroupList.add(questionnaireGroup);
            }
            questionnaireGroupRepository.saveAllAndFlush(questionnaireGroupList);
            logger.info("generated demo data for QuestionnaireGroup");

            logger.info("... generating {0} QuestionnaireProfile entities...",NUMBERQUESTPROFILETOGENERATE);
            List<QuestionnaireProfile> profileList = new ArrayList<>();
            for (int i = 1; i <= NUMBERQUESTPROFILETOGENERATE; i++) {
                QuestionnaireProfile profile = new QuestionnaireProfile();
                profile.setId((long) i);
                profile.setDescription(faker.aviation().airport());
                profileList.add(profile);
            }
            profileRepository.saveAllAndFlush(profileList);
            logger.info("generated demo data for QuestionnaireProfile");

            logger.info("... generating {0} Questionnaire entities...",NUMBERQUESTTOGENERATE);
            List<Questionnaire> questionnaireList = new ArrayList<>();
            List<QuestionnaireGroup> qgFromDb = questionnaireGroupRepository.findAll();
            List<QuestionnaireProfile> qpFromDb = profileRepository.findAll();
            for (int i = 0; i < NUMBERQUESTTOGENERATE; i++) {
                Questionnaire questionnaire = new Questionnaire();
                questionnaire.setName(faker.backToTheFuture().character());
                questionnaire.setVersion("Ver" + faker.regexify("[0-9]{2}"));
                questionnaire.setTitle(faker.chuckNorris().fact());
                questionnaire.setSubTitle(faker.aquaTeenHungerForce().character());
                questionnaire.setSearchText(faker.aviation().airport());
                questionnaire.setNotes(faker.shakespeare().hamletQuote());
                questionnaire.saveText(faker.backToTheFuture().character());
                questionnaire.setImage(faker.avatar().image());
                questionnaire.setImageAlt(faker.dragonBall().character());
                questionnaire.setInstructions(faker.lorem().characters(100));
                questionnaire.setCompilationTime(faker.number().numberBetween(0, 300));
                questionnaire.setForcedTerminationTime(faker.number().numberBetween(60, 120));
                questionnaire.setUsedSeconds(faker.number().numberBetween(60, 300));
                questionnaire.setStatus(faker.number().numberBetween(0, 5));
                int group=faker.number().numberBetween(0,NUMBERQUESTGROUPTOGENERATE-1);
                QuestionnaireGroup qg = questionnaireGroupRepository.getReferenceById(qgFromDb.get(group).getId());
                logger.info("Generato gruppo {0}",group);
                questionnaire.setQuestionnaireGroup(qg);
                int profile=faker.number().numberBetween(0,NUMBERQUESTPROFILETOGENERATE-1);
                QuestionnaireProfile qp = profileRepository.getReferenceById(qpFromDb.get(profile).getId());
                logger.info("Generato profile {0}",profile);
                questionnaire.setQuestionnaireProfile(qp);
                questionnaireList.add(questionnaire);
            }
            questionnaireRepository.saveAll(questionnaireList);
            logger.info("generated demo data for Questionnaire");
        };
    }
}
