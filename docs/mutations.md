# Mutation Colouring
When opening the [details view of a sample](../getting-started/#sample-details), mutations are displayed in different colours.

## Expected Mutations
In general, each haplogroup is defined by a set of expected mutations. Haplogrep informs users if the expected mutations for the tophit are included in the input sample.  

### Expected and Included ![](images/interface/expected_found.png)
Mutations displayed in green are mutations that are **expected by the tophit and also included** in the input sample.

### Expected But Not Included ![](images/interface/expected_not_found.png)
Mutations displayed in red are mutations that are **expected by the tophit but not included** in the input sample.

## Remaining Mutations
Remaining mutations of the input sample are separated by its type.

### Hotspots ![](images/interface/remaining_hotspot.png)
Hotspots mutations are defined by a high number of occurrences in the used phylogenetic tree.  

### Local Private Mutations ![](images/interface/remaining_local-private_mutation.png)
Local private mutations are mutations that are not associated with the tophit but are included in the phylogenetic tree for other haplogroups.

### Global Private Mutations ![](images/interface/remaining_global-private_mutation.png)
Global private mutations are not found at all in the phylogenetic tree.
