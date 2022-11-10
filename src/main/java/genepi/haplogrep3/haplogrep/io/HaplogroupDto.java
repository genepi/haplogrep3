package genepi.haplogrep3.haplogrep.io;

import java.util.List;
import java.util.Vector;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class HaplogroupDto {

    @JacksonXmlProperty(isAttribute = true)
	private String name = "";

	@JacksonXmlElementWrapper(useWrapping = false)
	@JsonProperty("haplogroup")
	public List<HaplogroupDto> haplogroups = new Vector<HaplogroupDto>();

	@JacksonXmlElementWrapper(localName = "details")
	@JsonProperty("poly")
	private List<String> details = new Vector<String>();

	public HaplogroupDto() {

	}

	public HaplogroupDto(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getDetails() {
		return details;
	}

	public void setDetails(List<String> details) {
		this.details = details;
	}

	public void addPoly(String poly) {
		details.add(poly);
	}

	public void setHaplogroups(List<HaplogroupDto> haplogroups) {
		this.haplogroups = haplogroups;
	}

	public List<HaplogroupDto> getHaplogroups() {
		return haplogroups;
	}

	public HaplogroupDto addHaplogroup(String name) {
		HaplogroupDto haplogroup = new HaplogroupDto(name);
		haplogroups.add(haplogroup);
		return haplogroup;
	}

}
