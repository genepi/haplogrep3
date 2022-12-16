# CovGrep CLI

CovGrep CLI enables you to align and classify SARS-CoV-2 FASTA sequences on your local workstation and integrate it into pipelines.


## Requirements


You will need the following things properly installed on your computer.

* Java 8 or higher

CovGrep works on Linux, macOS and Windows.

## Download and Install

### Linux or macOS

Download and install the latest version from our download page using the following commands:

```
wget https://github.com/genepi/covidgrep/releases/lates/covgrep.zip
```


Test the installation with the following command:

```sh
covgrep version
```

The documentation is available at [http://covgrep.readthedocs.io](http://covgrep.readthedocs.io).

All releases are also available on [Github](https://github.com/genepi/covgrep/releases).


### Windows

Download the [latest version](https://github.com/genepi/covgrep/releases/latest) of `covgrep-X.X.X-windows.zip` from our download page.

- Open the zip file and extract it content to a folder of choice.

- Open the command-line, navigate to your folder where you extracted the exe file.

- Test the installation with following command:

```sh
covgrep version
```

It will output something similar to the text shown below:

```sh
CovGrep 1.0.0
https://covgrep.i-med.ac.at
(c) 2021 Hansi Weissensteiner, Sebastian Schönherr, Lukas Forer
Built by lukas on 2021-09-01T11:31:10Z
```

Now you are ready to use CovGrep and to classify your first SARS-CoV-2 genomes.

## Classify your first SARS-CoV-2 sequences

For the classification with CovGrep, you need to provide a valid fasta file, and the output folder for the results, and the tree ID. The command is:

```sh
covgrep classify --fasta <Input File> --output <Output File> --tree <Tree Id>
```

### Input File

The input files needs to be a complete or partial fasta sequence of a SARS-CoV-2 genome. You can find data in [GenBank](https://www.ncbi.nlm.nih.gov/sars-cov-2/) for instance.

### Output File

The output file includes the sample Id, the classified clade, as well as the quality score determined for classification.

### Tree ID

As the underlying tree is not static, there will become more trees or updates available in the future. The following command can be used to get a list of all available trees:

```sh
covgrep trees
```

Moreover, you find the latest version of the tree in [GitHub](https://github.com/genepi/covgrep/blob/main/covgrep.yaml)

## Starting the CovGrep web-application locally

You can start the web-application locally, by starting CovGrep from the console with the parameter:

```sh
covgrep server
```
This will start the Javalin 4 web framework, running on top of a Jetty web-server. Subsequently you can open a browser of your choice and start the application there, by entering:

```sh
localhost:7000
```
Now you have all the possibilities as provided on the official web-service, with no data leaving your computer.
