# Annotations
Starting with Haplogrep 3, the software includes new features for evaluating haplogroups or variants. This includes the possibility to get a visual representation of each top-level haplogroup including the expected variants for each haplogroup (see [here](https://haplogrep.i-med.ac.at/phylogenies)) or (b) get frequencies for each selected variant in the samples details page.

## Clusters
We generated 33 top-level clusters according to PhyloTree and gnomAD and (a) group each haplogroup from the phylogeny (b) group each input sample into one of these clusters displayed within the summary dashboard.

![](images/interface/phylogeny_clusters.png)


## Population Frequencies
 We also display numerous frequencies for each variant mainly derived from [gnomAD](https://gnomad.broadinstitute.org/). Further annotations are available from the [Helix Mitochondrial database](https://www.helix.com/pages/mitochondrial-variant-database) and functional predictions from [MitImpact](https://mitimpact.css-mendel.it/).

![](images/interface/variant_annotations.png)

## Export 
Haplogrep allows to export annotations. Currently the following annotations are exported. 

* `ScorePhastCons100V` [MitImpact_db_3.1.0 - *PhastCons_100V*] 
* `ScoreMtoolBox` [MitImpact_db_3.1.0 - *MtoolBox_DS*]
* `ScoreAPOGEE` [MitImpact_db_3.1.0 - *APOGEE_score*]
* `PopFreqGnomeAD` [gnomAD v3.1 Frequencies - *popFreq*]
* `VafGnomADHom` [gnomAD v3.1 Annotations - *AF_hom*]
* `VafGnomADHet` [gnomAD v3.1 Annotations - *AF_het*]
* `Maplocus` [Haplogrep Annotation File]
* `AAC` [Haplogrep Annotation File]
* `ScoresMutPred` [Haplogrep Annotation File]
* `VafHelixHet` [Helix Mitochondrial database 20200327 - *Helix_vaf_het*]
* `VafHelixHom` [Helix Mitochondrial database 20200327 - *Helix_vaf_hom*]

*The source file and source column name are added in brackets.*