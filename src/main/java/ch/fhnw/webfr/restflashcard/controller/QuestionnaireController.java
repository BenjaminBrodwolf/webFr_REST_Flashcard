package ch.fhnw.webfr.restflashcard.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ch.fhnw.webfr.restflashcard.domain.Questionnaire;
import ch.fhnw.webfr.restflashcard.persistence.QuestionnaireRepository;

@RestController
@RequestMapping("/questionnaires")
public class QuestionnaireController {

    @Autowired
    private QuestionnaireRepository questionnaireRepository;

    @GetMapping
    public ResponseEntity<List<Questionnaire>> getAllQuestionnaires() {
        Sort sort = Sort.by(Direction.ASC, "id");
        List<Questionnaire> questionnaires = questionnaireRepository.findAll(sort);
        return new ResponseEntity<List<Questionnaire>>(questionnaires, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Questionnaire> getQuestionnaireById(@PathVariable String id) {
        
        Optional<Questionnaire> questionnaire = questionnaireRepository.findById(id);

        if(questionnaire.isPresent()){
            return new ResponseEntity<Questionnaire>(questionnaire.get(), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<Questionnaire> create (@Valid @RequestBody Questionnaire q,  BindingResult result){
     //has erros binding result

        if(result.hasErrors()){
            return new ResponseEntity<Questionnaire>(q, HttpStatus.PRECONDITION_FAILED);
        } 

        Questionnaire questionnaire = questionnaireRepository.save(q);

        return new ResponseEntity<Questionnaire>(questionnaire, HttpStatus.CREATED);
    }

    @PutMapping( value = "/{id}")
    public ResponseEntity<Questionnaire> update(@PathVariable String id, @Valid @RequestBody Questionnaire q, BindingResult result){

        if(result.hasErrors()){
            return new ResponseEntity<Questionnaire>(q, HttpStatus.PRECONDITION_FAILED);
        } 

        Optional<Questionnaire> qOptional = questionnaireRepository.findById(id);


        if(qOptional.isPresent()){
            Questionnaire toUpdateQuestionnaire = qOptional.get();
            toUpdateQuestionnaire.setTitle(q.getTitle());
            toUpdateQuestionnaire.setDescription(q.getDescription());

            Questionnaire updatedQestionnaire = questionnaireRepository.save(toUpdateQuestionnaire);

            return new ResponseEntity<Questionnaire>(updatedQestionnaire, HttpStatus.OK);
        } 
        
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Questionnaire> delete(@PathVariable String id){
        Optional<Questionnaire> qOptional = questionnaireRepository.findById(id);
        if(qOptional.isPresent()){
            questionnaireRepository.delete(qOptional.get());
            return new ResponseEntity<Questionnaire>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}