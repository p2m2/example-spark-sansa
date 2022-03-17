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

## run locally

=> add .master("local[*]")

```
java -cp ./target/scala-2.12/spark-msd-examples-assembly-0.1.0-SNAPSHOT.jar fr.inrae.training.spark.Main 
```

## run on msd cluster


``` 
scp ./target/scala-2.12/spark-msd-examples-assembly-0.1.0-SNAPSHOT.jar <user>@ara-unh-saroumane:~/
ssh <user>@ara-unh-saroumane
spark-submit --executor-memory 1G --num-executors 1 ../spark-msd-examples-assembly-0.1.0-SNAPSHOT.jar 
```

``` 
spark-submit --class fr.inrae.training.spark.Main --executor-memory 1G --num-executors 1 spark-sansa-read-turtle-example-assembly-0.1.0-SNAPSHOT.jar
```

## Sansa doc

http://sansa-stack.github.io/SANSA-Stack/
