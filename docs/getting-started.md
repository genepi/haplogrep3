# Getting Started

This guide shows how to classify your data with the Haplogrep webservice.

## Connect
Open the [web service](https://haplogrep.i-med.ac.at/haplogrep3) which runs at the Medical University of Innsbruck. If you want to run Haplogrep locally, please you can find the installation [here](../installation).

## Uploading Data
After connecting to our web service instance, you see the following welcome screen. You can now upload your in **FASTA**, **VCF** or **text-based** format. Haplogrep detects the file format automatically. Please choose the according file format to add additional input options.
Furthermore, you can select the distance function ([Kulczynski](../kulczynski), Hamming, Jaccard) and the used [phylogenetic tree](../trees).  

![](images/interface/welcome_screen.png)

## Classified Haplogroups
After clicking **Upload and Classify** the data is classified and displayed directly in the web interface. Each line displays a sample including (a) name, (b) haplogroup, (c) quality, (d) number of N positions, (e) coverage, (f) range and (g) input mutations. You can click on each sample to get sample details.

![](images/interface/data_classified.png)

## Sample Details
After clicking on one sample, a details view will open. In this view you will find all detected [Errors and Warnings](../errors-warnings), the expected / remaining mutations for the top hit haplogroup and the used ranges. Haplogrep calculates by default the top 20 hits for each sample. You will also see a list of further hits within the details view.

![](images/interface/sample_details.png)

## Sample Filtering
Haplogrep allows to filter your samples according to their status. The screenshot below shows all samples including detected warning messages within the input dataset.  

![](images/interface/data_filtered.png)

## Export Options
Haplogrep provides numerous export options. Currently you can export your data as a csv file, extended csv file including mutation details and data in fasta / MSA fasta format.

![](images/interface/export_options.png)
