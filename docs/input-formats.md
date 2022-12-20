# Input Formats
Haplogrep supports input data in a text-based (\*.hsd), VCF or fasta format.

#### FASTA
For alignment, [bwa version 0.7.17](https://github.com/lh3/bwa/releases/tag/v0.7.17) is used. For each input sequence, HaploGrep excludes positions from the tested range that are (1) not covered by the input fragment or (2) has marked with a N in the sequence.

#### hsd Format
You can also specify your profiles in the original HaploGrep **hsd** format, which is a simple tab-delimited file format consisting of 4 columns (ID, Range, Haplogroup and Polymorphisms).

#### VCF
You can upload a compressed multi-sample VCF file (vf.gz).

```
Sample1 1-16569 H100 263G 315.1C 750G	1041G	1438G	4769G	8860G	9410G	12358G	13656C	15326G	16189C	16192T	16519C
Sample2 1-16569 ? 73G	263G	315.1C 750G	1438G	3010A	3107C	4769G	5111T	8860G	10257T	12358G	15326G	16145A	16222T	16519C
```
For readability, the polymorphisms are also tab-delimited (so columns >= 4). A hsd example can be found [here](../data/H100.hsd).
