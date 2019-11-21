package ch.fhnw.webfr.restflashcard;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import ch.fhnw.webfr.restflashcard.TestUtil.QuestionnaireBuilder;
import ch.fhnw.webfr.restflashcard.domain.Questionnaire;
import ch.fhnw.webfr.restflashcard.persistence.QuestionnaireRepository;
import ch.fhnw.webfr.restflashcard.controller.QuestionnaireController;

@RunWith(SpringRunner.class)
@WebMvcTest(QuestionnaireController.class)
public class QuestionnaireControllerTest {
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
    private QuestionnaireRepository questionnaireRepositoryMock;

	@Before
    public void setUp() {
		Mockito.reset(questionnaireRepositoryMock);
    }
	
	@Test
	public void create_NewQuestionnaire_ShouldReturnOK() throws Exception {

		// Instanz zum Testen
		Questionnaire questionnaire = new QuestionnaireBuilder("1")
			.description("MyDescription")
			.title("MyTitle")
			.build();

		// Hier wird dem Mock mitgeteilt, was er in welcher Situation tun muss.
		when(questionnaireRepositoryMock.save(questionnaire)).thenReturn(questionnaire);


		mockMvc.perform(post("/questionnaires") // HTTP-POST Request auslösen
					 .contentType(MediaType.APPLICATION_JSON)
					 .content(TestUtil.convertObjectToJsonBytes(questionnaire))) // Request wird aufgesetzt
					 .andExpect(status().isCreated())
					 .andExpect(jsonPath("$.id", is("1"))) // Response wird überprüft
					 .andExpect(jsonPath("$.title", is("MyTitle")))
					 .andExpect(jsonPath("$.description", is("MyDescription")));

		// Test, ob Mock-Methode korrekt aufgerufen wurde
		Mockito.verify(questionnaireRepositoryMock, times(1)).save(questionnaire);
	}

	@Test
	public void createNoTitle_NewQuestionnaire_ShouldReturnPRECONDITION_FAILED() throws Exception {

		// Instanz zum Testen
		Questionnaire questionnaire = new QuestionnaireBuilder("1")
			.description("MyDescription")
			.title("")
			.build();

		// Hier wird dem Mock mitgeteilt, was er in welcher Situation tun muss.
		when(questionnaireRepositoryMock.save(questionnaire)).thenReturn(questionnaire);


		mockMvc.perform(post("/questionnaires") // HTTP-POST Request auslösen
					 .contentType(MediaType.APPLICATION_JSON)
					 .content(TestUtil.convertObjectToJsonBytes(questionnaire))) // Request wird aufgesetzt
					 .andExpect(status().isPreconditionFailed())
					 .andExpect(jsonPath("$.id", is("1"))) // Response wird überprüft
					 .andExpect(jsonPath("$.title", is("")))
					 .andExpect(jsonPath("$.description", is("MyDescription")));

		// Test, ob Mock-Methode korrekt aufgerufen wurde
		Mockito.verify(questionnaireRepositoryMock, times(0)).save(questionnaire);
	}
	
	@Test
	public void findAll_NewQuestionnaires_ShouldReturnOK() throws Exception {

		Questionnaire q1 = new QuestionnaireBuilder("1")
			.title("Titel1")
			.description("Desc1")
			.build();
		
		Questionnaire q2 = new QuestionnaireBuilder("2")
			.title("Titel2")
			.description("Desc2")
			.build();

		Questionnaire q3 = new QuestionnaireBuilder("3")
			.title("Titel3")
			.description("Desc3")
			.build();

		List<Questionnaire> questionnaires = new ArrayList<>();
		questionnaires.addAll(List.of(q1, q2, q3));
		
		for (Questionnaire questionnaire : questionnaires) {
			System.out.println(questionnaire.getId());
			System.out.println(questionnaire.getTitle());
			System.out.println(questionnaire.getDescription());
		}
		
		when(questionnaireRepositoryMock.findAll()).thenReturn(questionnaires);

		// for (int i = 0; i < questionnaires.size(); i++) {
		// 	String id = String.valueOf(i+1);
		// 	mockMvc.perform(get("/questionnaires") // HTTP-GET Request auslösen
		// 	.contentType(MediaType.APPLICATION_JSON)
		// 	.content(TestUtil.convertObjectToJsonBytes(questionnaires.get(i)))) // Request wird aufgesetzt
		// 	.andExpect(status().isOk())
		// 	.andExpect(jsonPath("$[" + i  + "].id", is( id ))) // Response wird überprüft
		// 	.andExpect(jsonPath("$[" + i  + "].title", is( "Titel"+ id)))
		// 	.andExpect(jsonPath("$[" + i  + "].description", is("Desc" + id )));
		// }
			
		 System.out.println(  questionnaires.get(0)  );
		 mockMvc.perform(get("/questionnaires") // HTTP-GET Request auslösen
		 	.contentType(MediaType.APPLICATION_JSON)
		 	.content(TestUtil.convertObjectToJsonBytes(questionnaires.get(0)))) // Request wird aufgesetzt
		 	.andExpect(status().isOk())
		 	.andExpect(jsonPath("$[0].id", is( "1" ))) // Response wird überprüft
		 	.andExpect(jsonPath("$[0].title", is( "Titel1")))
		 	.andExpect(jsonPath("$[0].description", is("Desc1")));
		


		Mockito.verify(questionnaireRepositoryMock, times(3)).saveAll(questionnaires);
	}

	@Test
	public void update_Questionnare_ShouldReturnOK() throws Exception {

		Questionnaire questionnaire = new QuestionnaireBuilder("1")
			.title("MyTitle")
			.description("MyDescription")
			.build();

		
		when(questionnaireRepositoryMock.save(questionnaire)).thenReturn(questionnaire);

		// mockMvc.perform(put("/questionnaires/1"))

	}

	@Test
	public void update_QuestionnareWrongID_ShouldReturnNotFound() throws Exception {

		
	}

	@Test
	public void delete_Questionnaire_ShouldReturnOK() throws Exception {

	}

	@Test
	public void delete_QuestionnaireWrongID_ShouldReturnNotFound() throws Exception {
		
	}
}
