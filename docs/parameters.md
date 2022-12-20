# Command-line Version

After you have successfully installed Haplogrep on your local system, you can now run it with the following command.

```
./haplogrep3 classify
```

## Required Parameters
The following parameters are mandatory.

|Parameter| Description|
|---|---|
|```--tree``` | Select one of the available trees |
|```--in``` | Please provide the input file name |
|```--out``` | Please provide an output name|

## Optional Parameters   

Besides the mandatory parameters, we also provide numerous parameters for specific use cases.

|Parameter| Description|
|---|---|
|```--metric```, ```distance```| To **change the classification metric** to Hamming Distance (```hamming```) or Jaccard (```jaccard```) add this parameter (Default: Kulczynski Measure).|
|```--extend-report```| For additional information on SNPs (e.g. found or remaining polymorphisms) please add the `--extend-report` flag (Default: off).|
|```--tree```| Specify one of the installed trees.|
|```--chip```| If you are using **genotyping arrays**, please add the `--chip` parameter to limit the range to array SNPs only (Default: off, VCF only). To get the same behaviour for hsd files, please add **only** the variants to the range, which are included on the array or in the range you have sequenced (e.g. control region). Range can be sepearted by a semicolon `;`, both ranges and single positions are allowed (e.g. 16024-16569;1-576;8860). |
|```--skip-alignment-rules```|  Add this option to skip our rules that fixes the mtDNA nomenclature for fasta import. Click [here](#mtdna-nomenclature) for further information. Applying the rules is the default option since v2.4.0|
|```--hits``` |  To export the **best n hits** for each sample add the `--hits` parameter. By default only the tophit is exported.|
|```--write-fasta``` |  Write results in fasta format.|
|```--write-fasta-msa``` |  Write multiple sequence alignment.|
|```--hetLevel=<value>``` |  Add heteroplasmies with a level > <value> from the VCF file to the profile (default: 0.9). |
