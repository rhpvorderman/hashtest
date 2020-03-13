# hashtest

Test hashing algorithms in Scala.

This repository was made to test implementations of the 
[XXHash](www.xxhash.com) algorithms (XXH64 and XXH32) for [Cromwell
](https://github.com/broadinstitute/cromwell).

There are currently two implementations of the xxhash algorithm in Java

* [lz4-java](https://github.com/lz4/lz4-java)
* [OpenHFT's zero allocation hashing](
  https://github.com/OpenHFT/Zero-Allocation-Hashing)

Both are tested in this repo.

# Benchmarking

## Tested algorithms
OpenHFT does not provide a streaming version of the  xxhash algorithm. This 
makes it unsuitable for very big files. An attempt to convert the given 
algorithm into a streaming algorithm using the seed has failed. 

Lz4-java's xxh64 and xxh32 algorithms were benchmarked against apache's
commons-codec digestutils md5 method.

For comparison also native C-implementations were tested on the test machine.

## Test machine

* CPU: AMD Ryzen 5 3600 processor. 6 cores, 12 threads. Base clock 3.6 ghz, boost clock: 4.2 ghz.
* Memory: 2 x 16 GB DDR4-3200 Memory
* Storage: Kingston A2000 1TB NVME SSD
* Operating System: Debian 10 Buster with `xxhash` package installed.

## Test methodology

The native C implementations are tested using [hyperfine](
https://github.com/sharkdp/hyperfine), a tool which makes it very easy to
perform benchmarks. 

The java implementations are tested using the code in 
`src/test/scala/net/biowdl/hashtesttest`.

The hash algorithms were run on the 3,4 gb BWA index that is provided by the
NCBI for the HG38 reference genome. It can be downloaded [here](
ftp://ftp.ncbi.nlm.nih.gov/genomes/all/GCA/000/001/405/GCA_000001405.15_GRCh38/seqs_for_alignment_pipelines.ucsc_ids/GCA_000001405.15_GRCh38_full_plus_hs38d1_analysis_set.fna.bwa_index.tar.gz).

Running the `get_test_files.sh` script will automatically download the big
test file to the correct place for testing.

For each test 3 warmup runs were performed to negate any effects of kernel
file caching. For each test 10 total tests were run over which the average was
calculated.



