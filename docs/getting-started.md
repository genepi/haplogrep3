# Getting Started

This guide shows how to classify your data with the Haplogrep webservice.

## Open
Open the [web service](https://haplogrep.i-med.ac.at/haplogrep3), which runs at the Medical University of Innsbruck. If you want to run it locally, please read about the installation [here](../installation).

## Uploading Data
After opening our web service, you see the following welcome screen. You can now upload your data in **FASTA**, **VCF** or **text-based** format. The following input options are available:

* Haplogrep detects the file format automatically. Please note that additional input options appear (e.g. for VCF), when selecting a specific input format.
* Select one of the available distance functions ([Kulczynski](../kulczynski), Hamming, Jaccard).
* Select one of the available trees. Learn more about trees [here](../trees).  

![](images/interface/welcome_screen.png)

## Classifying Haplogroups
After clicking **Upload and Classify** the data is classified and displayed directly in the web interface. Each line displays a sample including the (a) name, (b) haplogroup, (c) quality, (d) number of N positions, (e) covered positions, (f) range and (g) input mutations. You can click on each sample to receive sample details.

![](images/interface/data_classified.png)

## Sample Details
After clicking on one sample, a details view opens. In this view you will find the following information:

* All detected [errors and warnings](../errors-warnings) highlighted in yellow.
+ The haplogroup tophit (here: W1c) including the reached quality score in percentage. This is calculated by using one of our provided distance metrics.  
* The expected mutations for the tophit (W1c) marked in *green* and *red*. *Green* means that the mutation is required for the tophit and has also been found in the input sample. *Red* means that the mutation is required by the tophit but was not found in the input sample.
* The remaining mutations from the input sample. Hotspots are marked in *green*, local private mutations in *blue* and global private mutations in *red*.
* The detected ranges for the input sample.
* The 20 tophits for each sample (denoted by "Other Hits").

![](images/interface/sample_details.png)

## Sample Filtering
Haplogrep allows to filter your samples according to their status. The screenshot below shows all samples including warnings within the input dataset.  

![](images/interface/data_filtered.png)

## Export Options
Haplogrep provides numerous export options. Currently you can export your data as a (a) csv file, (b) extended csv file including mutation details, (c) fasta or (d) MSA fasta file.

![](images/interface/export_options.png)
