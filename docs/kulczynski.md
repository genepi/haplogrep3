# Kulczynski Metric

Haplogrep supports input data in a text-based (\*.hsd), VCF or fasta format.

Here we show how Haplogrep's default distance metric (so called Kulczynski measure) works on a concrete example. (Note: this blog post has been updated after receiving a correction from Chris Simpson, Thanks!)

So let's say this is your input sample in hsd format:

```
test 16024-16569;1-576; ? 73G 263G 285T 315.1C 455.1T 523G 524T 16051G 16129A 16188.1C 16249C 16264G
```
The Kulczynski measure is defined as follows:
```
(HaplogroupWeight + SampleWeight) * 0.5
```

HaploGrep applies this formula to all haplogroups in Phylotree and finally returns the overall best hit. In this example, I'll calculate the measure only for the best hit, this is in our case U1a2.

1) First, we have to calculate the HaplogroupWeight:
```
HaplogroupWeight = FoundPolymorphismsWeight/ExpectedPolymorphismsWeight
```

Found Polymorphisms: Polymorphisms from the input sample that are detected (or found) in the currently tested haplogroup (i.e. in our case U1a2)

Expected Polymorphisms: Polymorphisms that are included (or expected) in the currently tested haplogroup (i.e. in our case U1a2).

Found polymorphisms + weights: ```455.1T (6.7), 263G (8.8), 285T (10.0), 16249C (4.5), 16129A->2.6, 73G (5.6)```

Expected polymorphisms + weights: ```455.1T (6.7), 263G (8.8), 285T (10.0), 16189C (2.0), 16249C (4.5), 16129A (2.6), 73G (5.6)```

FoundPolymorphismsWeight: ```6.7 + 8.8 + 10 + 4.5 + 2.6 + 5.6 = 38.2```

ExpectedPolymorphismsWeight:```6.7 + 8.8 + 10 + 2 + 4.5 + 2.6 + 5.6 = 40.2```

HaplogroupWeight:```41.5 / 43.5 = 0.9540229885```

As you can see only 16189C is not found but expected by the haplogroup.

2) Second, we have to calculate the SampleWeight:
```
SampleWeight = FoundPolymorphismsWeight/SamplePolymorphismsWeight
```
Found Polymorphisms: see above

Sample Polymorphisms: Polymorphisms that are included in the sample and are falling into the specified range (2nd column of hsd, in our case 16024-16569;1-576;)

Sample polymorphisms (weights):```16264G (0.0), 16188.1C (0.0), 263G (8.8), 311C (0.0), 16129A (2.6), 16051G (4.5), 455.1T (6.7), 523G (0.0), 285T (10.0), 524T (0.0), 16249C (4.5), 73G (5.6)```

SamplePolymorphismsWeight: ```8.8 + 2.6 + 4.5 + 6.7 + 10 + 4.5 + 5.6 = 42.7```

SampleWeight: ```41.5 / 46 = 0.90217391304```

As you can see 16051G is included in the sample but not required by the haplogroup.

3) Third use the calculate weights and calculate the final measure:
```
(HaplogroupWeight + SampleWeight) * 0.5
(0.90217391304 + 0.9540229885) / 2 = 0.92809845077
```
The best hit (U1a2) has a quality of 0.928. We also integrated that as an automatic test case here.

Keep in mind that in a real life scenario the quality of all haplogroups are calculated, sorted and the best 50 hits are returned.
