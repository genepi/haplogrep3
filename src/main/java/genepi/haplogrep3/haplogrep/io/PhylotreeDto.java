package genepi.haplogrep3.haplogrep.io;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "phylotree")
public class PhylotreeDto {

	@JsonIgnore
	private Set<String> allMutations = new HashSet<String>();

	@JacksonXmlElementWrapper(useWrapping = false)
	@JsonProperty("haplogroup")
	public List<HaplogroupDto> haplogroups = new Vector<HaplogroupDto>();

	public List<HaplogroupDto> getHaplogroups() {
		return haplogroups;
	}

	public void setHaplogroups(List<HaplogroupDto> haplogroups) {
		this.haplogroups = haplogroups;
	}

	public HaplogroupDto addHaplogroup(String name) {
		HaplogroupDto haplogroup = new HaplogroupDto(name);
		haplogroups.add(haplogroup);
		return haplogroup;
	}

	public Set<String> getAllMutations() {
		return allMutations;
	}

	public void setAllMutations(Set<String> allMutations) {
		this.allMutations = allMutations;
	}

	public void addMutation(String mutation) {
		allMutations.add(mutation);
	}

}
