[![Java CI with Maven](https://github.com/genepi/haplogrep3/actions/workflows/maven.yml/badge.svg)](https://github.com/genepi/haplogrep3/actions/workflows/maven.yml)

# Haplogrep 3

Free mtDNA Haplogroup Classification Service


## Requirements

You will need the following things properly installed on your computer.

* Java 11 or higher

Haplogrep works on Linux, macOS and Windows.

## Download and Install

### Linux or macOS

Download and install the latest version from our download page using the following commands:

```
wget https://github.com/genepi/haplogrep3/releases/latest/haplogrep3-1.0.0-linux.zip
```

or download the [latest version](https://github.com/genepi/haplogrep3/releases/latest) version.

- Open the zip file and extract its content to a folder of choice.

- Open the command-line, navigate to your folder where you extracted the file.

- Test the installation with the following command:

```sh
haplogrep3 version
```

The documentation is available at [http://haplogrep.readthedocs.io](http://haplogrep.readthedocs.io).

All releases are also available on [Github](https://github.com/genepi/haplogrep3/releases).


### Windows

Download the [latest version](https://github.com/genepi/haplogrep3/releases/latest) of `haplogrep3-1.0.0-windows.zip` from our download page.

- Open the zip file and extract its content to a folder of choice.

- Open the command-line, navigate to your folder where you extracted the exe file.

- Test the installation with following command:

```sh
haplogrep3 version
```

It will output something similar to the text shown below:

```sh
Haplogrep 3
https://haplogrep-med.ac.at
(c) 2021 Hansi Weissensteiner, Sebastian Schönherr, Lukas Forer
Built by lukas on 2021-09-01T11:31:10Z
```

Now you are ready to use Haplogrep 3 and to classify your first mtDNA haplogroups.

## Classify your first Haplogrep3 

For the classification with Haplogrep 3, you need to provide a valid fasta file, and the output folder for the results, and the tree ID. The command is:

```sh
haplogrep3 classify --hsd <Input File> --output <Output File> --tree <Tree Id>
```

### Input File

The input files needs to be a complete or partial fasta sequence of a SARS-CoV-2 genome. You can find data in [GenBank](https://www.ncbi.nlm.nih.gov/sars-cov-2/) for instance.

### Output File

The output file includes the sample Id, the classified clade, as well as the quality score determined for classification.

### Tree ID

As the underlying tree is not static, there will become more trees or updates available in the future. The following command can be used to get a list of all available trees:

```sh
haplogrep3 trees
```

Moreover, you find the latest version of the tree in [GitHub](https://github.com/genepi/covgrep/blob/main/covgrep.yaml)

## Starting the Haplogrep 3 web-application locally

You can start the web-application locally, by starting Haplogrep from the console with the parameter:

```sh
haplogrep3 server
```
This will start the Javalin 4 web framework, running on top of a Jetty web-server. Subsequently you can open a browser of your choice and start the application there, by entering [http://localhost:7000](http://localhost:7000).

Now you have all the possibilities as provided on the official web-service, with no data leaving your computer.

## Contact

This software was developed at the [Institute of Genetic Epidemiology](https://genepi.i-med.ac.at/), [Medical University of Innsbruck](https://i-med.ac.at/)

![](https://avatars2.githubusercontent.com/u/210220?s=30) [Lukas Forer](mailto:lukas.forer@i-med.ac.at) ([@lukfor](https://twitter.com/lukfor))

![](https://avatars2.githubusercontent.com/u/1931865?s=30) [Hansi Weissensteiner](mailto:hansi.weissensteiner@i-med.ac.at) ([@whansi](https://twitter.com/whansi))

![](https://avatars2.githubusercontent.com/u/1942824?s=30) [Sebastian Schoenherr](mailto:sebastian.schoenherr@i-med.ac.at) ([@seppinho](https://twitter.com/seppinho))
