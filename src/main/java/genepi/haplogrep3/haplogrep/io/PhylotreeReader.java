package genepi.haplogrep3.haplogrep.io;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;

public class PhylotreeReader {

	public static PhylotreeDto readFromJson(String filename) throws Exception {

		Gson gson = new Gson();
		LinkedTreeMap o = (LinkedTreeMap) gson.fromJson(new FileReader(filename), Object.class);

		PhylotreeDto phylotree = new PhylotreeDto();
		buildTree("root", o.get("tree"), phylotree, null);

		return phylotree;

	}

	public static PhylotreeDto readFromXml(String filename) throws JsonParseException, JsonMappingException, IOException {
		
		XmlMapper xmlMapper = new XmlMapper();
		PhylotreeDto phylotree = xmlMapper.readValue(new File(filename), PhylotreeDto.class);
		
		return phylotree;
		
	}
	
	protected static void buildTree(String parent, Object tree, PhylotreeDto phylotree,
			HaplogroupDto parentHaplogroup) {

		String name = parent;

		if (((LinkedTreeMap) ((LinkedTreeMap) tree).get("branch_attrs")).get("labels") != null) {

			Object clade = ((LinkedTreeMap) ((LinkedTreeMap) ((LinkedTreeMap) tree).get("branch_attrs")).get("labels"))
					.get("clade");

			if (clade != null) {
				name = clade.toString();
				parent = name;
			} else {
				name = parent + "_" + UUID.randomUUID();
			}

		} else {
			name = parent + "_" + UUID.randomUUID();
		}

		HaplogroupDto haplogroup = null;
		if (parentHaplogroup != null) {
			haplogroup = parentHaplogroup.addHaplogroup(name);
		} else {
			haplogroup = phylotree.addHaplogroup(name);
		}

		if (((LinkedTreeMap) ((LinkedTreeMap) tree).get("branch_attrs")).get("mutations") != null) {
			Object mutations = (Object) ((LinkedTreeMap) ((LinkedTreeMap) tree).get("branch_attrs")).get("mutations");
			if (mutations != null && ((LinkedTreeMap) mutations).get("nuc") != null) {
				for (Object mutation : (ArrayList) ((LinkedTreeMap) mutations).get("nuc")) {
					// remove reference allele and rename deletions
					String mutationName = mutation.toString().substring(1).replace("-", "d");
					haplogroup.addPoly(mutationName);
					phylotree.addMutation(mutationName);
				}
			}
		}

		ArrayList children = (ArrayList) (((LinkedTreeMap) tree).get("children"));
		if (children != null) {
			for (Object child : children) {
				buildTree(parent, child, phylotree, haplogroup);
			}
		}

	}

}
