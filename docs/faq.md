# FAQ

### Which data formats are supported?

CovGrep accepts FASTA files from complete or partial sequenced SARS-CoV-2 DNA sequences.

### What data is used for the tree?

We used the data from [Nextstrain](https://github.com/nextstrain/ncov/blob/master/defaults/clades.tsv) and the list of shared variants in [CoVariants](https://covariants.org/shared-mutations) to generate the phylogenetic tree. You can click on "Phylogeny" on the top-left navigation bar.

### Where can I find the used tree ids?

When starting CovGrep from the Command-Line Interface (CLI) you need to provide a Tree ID. The following command can be used to get a list of all available trees:

```sh
covgrep trees
```

This information can also be found in [GitHub](https://github.com/genepi/covgrep/blob/main/covgrep.yaml).

### Can I use/adapt an own tree?

Please get in contact with [Hansi Weissensteiner](mailto:hansi.weissensteiner@i-med.ac.at) if you want to use a different underlying phylogenetic tree in your local version.
