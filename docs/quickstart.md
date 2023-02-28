# Quickstart

There are 3 possibilities how to classify your profiles with Haplogrep.

## Hosted web service
We provide Haplogrep 3 as a [web-service](https://haplogrep.i-med.ac.at) at the Medical University of Innsbruck. The service allows you upload the data to our service and run Haplogrep without any registration. Your input data is deleted right after classification. The results are available via a unique and shareable link for 7 days.    


### Local web service

[Download the latest version](installation.md) of Haplogrep and run the following command:

```sh
./haplogrep3 server
```
This will start a local version of Haplogrep at [http://localhost:7000](http://localhost:7000).


### Haplogrep CLI

[Download the latest version](installation.md) of Haplogrep and run the following command:

```sh
./haplogrep3 classify --tree <tree-id> --input <input-file> --output <output-file>
```

For the classification with Haplogrep on the command-line, you have to specify the used classification tree and provide a valid input and output file. Click [here](../trees.md) to learn more about trees. To get a list of available trees enter the following command:
```sh
./haplogrep3 trees
```
