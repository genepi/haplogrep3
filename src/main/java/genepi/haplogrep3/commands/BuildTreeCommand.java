package genepi.haplogrep3.commands;

import genepi.haplogrep3.haplogrep.io.PhylotreeDto;
import genepi.haplogrep3.haplogrep.io.PhylotreeReader;
import genepi.haplogrep3.haplogrep.io.PhylotreeWriter;
import genepi.haplogrep3.haplogrep.io.VariantsOfConcern;
import genepi.haplogrep3.haplogrep.io.WeightsWriter;
import picocli.CommandLine.Option;

public class BuildTreeCommand extends AbstractCommand {

	@Option(names = { "--input" }, description = "input nextstrain tree.json file", required = true)
	private String input;

	@Option(names = { "--output" }, description = "output haplogrep xml file", required = true)
	private String output;

	@Option(names = { "--output-weights" }, description = "output haplogrep xml file", required = true)
	private String outputWeights;
	
	@Option(names = { "--voc" }, description = "variants of concerns ", required = true)
	private String inputVOC;

	/**
	 * @return the input
	 */
	public String getInput() {
		return input;
	}

	/**
	 * @param input the input to set
	 */
	public void setInput(String input) {
		this.input = input;
	}

	/**
	 * @return the output
	 */
	public String getOutput() {
		return output;
	}

	/**
	 * @param output the output to set
	 */
	public void setOutput(String output) {
		this.output = output;
	}

	/**
	 * @return the outputWeights
	 */
	public String getOutputWeights() {
		return outputWeights;
	}

	/**
	 * @param outputWeights the outputWeights to set
	 */
	public void setOutputWeights(String outputWeights) {
		this.outputWeights = outputWeights;
	}

	@Override
	public Integer call() throws Exception {

		PhylotreeDto phylotree = PhylotreeReader.readFromJson(input);

		PhylotreeWriter.writeToXml(output, phylotree);

		VariantsOfConcern variantsOfConcern = new VariantsOfConcern(inputVOC);
		
		WeightsWriter.writeToTxt(outputWeights, phylotree, variantsOfConcern);

		return 0;

	}

}