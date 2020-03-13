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

The md5sum, xxh64sum and xxh32sum algorithms where tested.

### Java implementations
OpenHFT does not provide a streaming version of the  xxhash algorithm. This 
makes it unsuitable for very big files. An attempt to convert the given 
algorithm into a streaming algorithm using the seed has failed. 

Lz4-java's xxh64 and xxh32 algorithms were benchmarked against apache's
commons-codec digestutils md5 method.

### Native c-implementations
For comparison also native C-implementations were tested on the test machine.
- md5sum (GNU coreutils) 8.30, from the Debian coreutils package.
- xxh64sum 0.6.5 (64-bits little endian), by Yann Collet, from the Debian
  xxhash package.
- xxh32sum 0.6.5 (64-bits little endian), by Yann Collet, from the Debian 
  xxhash package.


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

## Results 

### Native implementations
These were run using the native C tools provided on Debian Buster

- md5sum results. Average=4875.9ms, min=4833.3ms, max=4925.5ms
- xxh64sum results. Average=472.5ms, min=466.3ms, max=485.8ms
- xxh32sum results. Average=696.8ms, min=686.6ms, max=714.5ms

### Java implementations
- md5sum results. Average=8491.1ms, min=8419.0ms, max=8549.0ms
- xxh64sum results. Average=582ms, min=580ms, max=585ms
- xxh32sum results. Average=798ms, min=791ms, max=805ms

## Conclusion

Apache's commons-codec version of md5 is not implemented well in this project. 
It suffers from a relatively big performance degradation (almost 2x slower) 
than the native C implementation.

The xxh64sum and xxh32sum implementation's of [lz4-java](
https://github.com/lz4/lz4-java) are quite excellent. The overhead of approx 
120ms on a 3.4 GB file might percentually be ~25% but it is unsure how much of
this is due to the benchmark code. ~25% is also an acceptable hit when going
from native C to the JVM.

# Effect of buffer sizes

This benchmarks the [lz4-java](https://github.com/lz4/lz4-java) 
streaming implementations and tries to check the effects of different 
buffersizes. The above testing methodology was used. 
The buffer sizes were adapted in the code and the tests were run.
Only averages are reported.

## Results
Buffer size are in kb (1024 bytes). Results are reported in ms

Buffersize | xxh64sum | xxh32sum
---|---|---
32kb | 667ms | 870ms
64kb | 602ms | 823ms
96kb | 577ms | 796ms
128kb | 588ms | 808ms
256kb | 592ms | 809ms
1024kb | 566ms | 778ms
3072kb | 616ms | 841ms

Increasing the 
buffersize from 32kb to 64kb netted a performance advantage of about ten 
percent. Going from 64 to 96 kb achieved about 5 percent more performance.
Going up from 96 to 128 kb degraded performance by about 2%. Going up from 
128kb to 256 kb had a negligible impact on performance. Going up even further
to 1024kb gets the optimal result for this machine. Going up to 3072kb 
degrades performance again.

## Conclusion
It is better to pick a default buffer size that is slightly too big, than one 
that is slightly too small. Too small buffer sizes have a much more negative 
impact than too big ones.

Picking very big buffers has a negative impact on memory usage. In light of 
these results, the 128kb buffer seems to be the optimal choice. It will not 
be too small on most machines (given the test machine used was a 2019 machine),
while it will also be not too big for most machines.
