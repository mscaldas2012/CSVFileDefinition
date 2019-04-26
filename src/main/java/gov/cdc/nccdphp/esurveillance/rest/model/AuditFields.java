package gov.cdc.nccdphp.esurveillance.rest.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;
/**
 * Audit fields abstract class inherited by other model classes. 
 * @CreatedDate, @LastModifiedDate are populated by Spring Data
 * @CretedBy, @LastModifiedBy are populated by AuditAwareImpl class using Spring Data 
 * @JsonIgnore ignores the fields being populated by @RequestBody by Jackson and Swagger docs
 * 
 * @author hxo5
 *
 */
@Data
public abstract class AuditFields {
	@CreatedBy
	@JsonProperty("_created_by")
	private String createdBy;
	
	@CreatedDate
	@JsonProperty("_created_datetime")
	private Date createdTime;
	
	@LastModifiedBy
	@JsonProperty("_updated_by")
	private String updatedBy;
	
	@LastModifiedDate
	@JsonProperty("_updated_datetime")
	private Date updatedTime;
	
	@JsonProperty("_record_version")
    private long recordVersion = 0L;

    @JsonProperty("_active")
	private boolean active = true;
}
