# Errors and Warnings

Haplogrep includes different warnings and errors. Please find a list of currently applied measures.

## Errors

* The detected haplogroup quality is low. Sample is marked red. Quality <=80%
* The expected haplogroup is not a super group of the detected haplogroup
* Common rCRS polymorphism not found! The sample seems not properly aligned to rCRS
* The sample seems to be aligned to RSRS. Haplogrep only supports rCRS aligned samples
* The sample misses >2 expected polymorphisms

## Warnings

* Fasta Alignment check:  positions to recheck
* The detected haplogroup does not match the expected haplogroup but represents a valid sub haplogroup
* The sample shows ambiguous best results
* The detected haplogroup quality is moderate. Sample is marked yellow. Quality <= 90% and > 80%
* The sample contains  polymorphimsms that are equal to the reference / The sample contains variants according the rCRS
* The sample contains >2 global private mutation(s) that are not known by Phylotree
* The sample contains >=2 local private mutation(s) associated with other Haplogroups
* Different haplogroup with 2 local private remaining mutation(s) found
* The sample contains undetermined variants N
