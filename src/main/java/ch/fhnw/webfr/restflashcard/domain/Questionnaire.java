package ch.fhnw.webfr.restflashcard.domain;

import org.hibernate.validator.constraints.Range;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import com.google.common.base.Objects;

@Document(collection="questionnaires")
public class Questionnaire {
    @Id
    private String id;
	
	@Size(min = 3, max = 20)
    private String  title;
	
	@Size(min = 5, max = 100)
	private String description;
    
    public void setId(String id) {
		this.id = id;
	}
    
    public String getId() {
		return id;
	}

	public void setTitle(String title) {
		this.title = title;
	}
    
    public String getTitle() {
		return title;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	@Override
	public int hashCode(){
		return Objects.hashCode(this.id, this.title, this.description);
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Questionnaire){
			final Questionnaire other = (Questionnaire) obj;
			return Objects.equal(id, other.id)
			&& Objects.equal(title, other.title)
			&& Objects.equal(description, other.description);

		} else {
			return false;
		}
	}

}
