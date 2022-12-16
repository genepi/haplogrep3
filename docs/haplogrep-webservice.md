# HaploGrep Webservice

HaploGrep Webservice provides a free mtDNA haplogroup classification service. You can upload sequences in FASTA format and receive haplogroup results in return. Our server uses a minimal consensus phylogenetic tree that uses the same nomenclature as [Nextclade](https://clades.nextstrain.org/). For all uploaded datasets an additional QC is performed, by checking for the presence of ambiguous (Ns, Y, R, ...) nucleotides, input range, check for expected and missing nucleotides per clade, as well as translation of amino-acids.

**To use the HaploGrep Webservice, no registration is required.**

## Classify your first SARS-CoV-2 sequences

Go to [covgrep.i-med.ac.at](https:\\covgrep.i-med.ac.at) and choose a fasta (or multifasta)- file(s) for uploading of up to 30 MB (corresponds ~1,000 complete SARS-CoV-2 genomes). The latest phylogenetic tree is provided, but can be also changed in an own drop-down menu. Click "Upload and Classify"

## Input files

The input files can be single fasta files or multi-fasta files, of complete, near-complete or partial SARS-CoV-2 sequences. [GenBank](https://www.ncbi.nlm.nih.gov/labs/virus/vssi/#/virus?SeqType_s=Nucleotide&VirusLineage_ss=SARS-CoV-2,%20taxid:2697049) provides over 1 million fasta files.

## Output Report

After upload and classification, an interactive table opens, with the informations per sample in rows:


## Export Results

TODO
