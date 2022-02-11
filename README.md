# example-spark-sansa

## download sansa
``` 
sbt downloadSansaJar
```

## run test

``` 
sbt test
```

## run assembly

``` 
sbt assembly
```

## run on msd cluster

``` 
spark-submit --executor-memory 1G --num-executors 1 ../test-spark-assemblye.jar 
```