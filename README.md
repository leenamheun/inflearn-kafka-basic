
# Kafka Inflearn Excersise

인프런 강의 실습내용에 대한 예제 코드 입니다.  
실습에서 제공하는 Java코드를 Kotlin으로 작성한 내용입니다.
인프런 강의 실습에 대한 예제 코드는 아래의 github에서 다운로드 할 수 있다.  
https://github.com/bjpublic/apache-kafka-with-java

---

## Producer
### Consumer console 조회
1. 카프카 콘솔 컨슈머를 실행할 수 있는 바이너리 디렉토리로 간다.
2. bin/kafka-server-start.sh config/server.properties 실행
3. 어플리케이션 실행 후 콘솔을 통해 데이터가 잘 전송되었는지 확인

### 메시지 전송 (key x)
SimpleProducer main()함수를 실행한다.
~~~ 
   bin/kafka-console-consumer.sh --bootstrap-server my-kafka:9092 --topic test --from-beginning 
~~~

### key-value과 포함된 메시지
ProducerWithKeyValue main()함수를 실행한다.
- key/value 확인
~~~ 
    bin/kafka-console-consumer.sh --bootstrap-server my-kafka:9092 \
     --topic test \
     --property print.key=true \
     --property key.separator="-" \
     --from-beginning 
 ~~~

### 특정 파티션에 저장하는 메시지
ProducerExactPartition main()함수를 실행한다.

### Custom Partitioner 이용
ProducerWithCustomPartitioner main()함수를 실행한다.  
Pankyo라는 특정 메시지키가 들어오면 파티션0에 전송한다.

### 레코드 전송 결과를 확인하는 프로듀서
ProducerSyncCallback main()함수를 실행한다.
~~~
    # test topic, 3번 partiton, 1번 offset
    [main] INFO ProducerSyncCallback - test-3@1
~~~
* acks=0으로 설정했을 시
~~~
    # 응답을 받지 않았기 때문에 offset번호는 알 수 없다. -1로 반환
    [main] INFO ProducerSyncCallback - test-3@-1
~~~

### 프로듀서의 안전한 종료
close()메소드를 사용하면 어큐뮤레이터에 저장되어있는 모든 데이터를 카프카 클러스터로 전송한다.
~~~
    producer.close()
~~~

---

## Consumer
### 메시지 전송 (key x)
SimpleConsumer main()함수를 실행한다.
~~~ 
bin/kafka-console-producer.sh --bootstrap-server my-kafka:9092 --topic test  
~~~

### 수동 커밋 - 동기 오프셋 커밋 컨슈머
SyncCommitConsumer main()함수를 실행한다.

### 수동 커밋 - 비동기 오프셋 커밋 컨슈머
ASyncCommitConsumer main()함수를 실행한다.

### 리밸런스 리스너를 가진 컨슈머 
ReBalanceConsumer main()함수를 실행한다.

### 컨슈머 어플리케이션의 안전한 종료
ShutdownConsumer main()함수를 실행한다.  
프로세스를 종료시켜 컨슈머가 안전히 종료되는지 확인한다.
~~~
//프로세스 조회 및 아이디 확인
ps -ef | grep ShutdownConsumer
//프로세스 종료
kill -term {processId}
~~~

---

## Streams
### 필터링 스트림즈 애플리케이션
토픽을 생성한다
~~~
bin/kafka-topics.sh --bootstrap-server my-kafka:9092 --topic stream_log --create
bin/kafka-topics.sh --bootstrap-server my-kafka:9092 --topic stream_log_filter --create
~~~ 
FilterStreams main()함수를 실행한다.  
두 개의 창을 띄어 하나는 토픽을 추가하고 하나는 텍스트 길이가 5개 이상인 토픽이 저장되는 것을 확인한다.   
토픽에 데이터를 전송한다.
~~~
bin/kafka-console-producer.sh --bootstrap-server my-kafka:9092 --topic stream_log
~~~
메시지 값의 길이가 5개 이상인 데이터만 아래의 토픽에 저장된다. 
~~~
bin/kafka-console-consumer.sh --bootstrap-server my-kafka:9092 --topic stream_log_filter
~~~

### KStreams와 KTable의 조인
토픽을 생성한다.   
코파티셔닝이 만족되기 위해 파티션이 3개이고 디폴트 파티셔닝 전략으로 생성한다. 
~~~
bin/kafka-topics.sh --create --bootstrap-server my-kafka:9092 \
 --partitions 3 \
 --topic address

 bin/kafka-topics.sh --create --bootstrap-server my-kafka:9092 \
 --partitions 3 \
 --topic order

 bin/kafka-topics.sh --create --bootstrap-server my-kafka:9092 \
 --partitions 3 \
 --topic order_join
~~~
KStreamsJoinKTable main()함수를 호출한다.   
address와 order토픽에 데이터를 추가한다
~~~
bin/kafka-console-producer.sh --bootstrap-server my-kafka:9092 \
 --topic address \
 --property "parse.key=true" \
 --property "key.separator=:"
 
>namni:Seoul
>somin:Busan
~~~
~~~
bin/kafka-console-producer.sh --bootstrap-server my-kafka:9092 \
 --topic order \
 --property "parse.key=true" \
 --property "key.separator=:"
 
>somin:iPhone
>namni:Galaxy
~~~
조회
~~~
bin/kafka-console-consumer.sh --bootstrap-server my-kafka:9092 \
 --topic order_join \
 --property print.key=true \
 --property key.separator=":" \
 --from-beginning
 
somin:iPhone send to Busan
namni:Galaxy send to Seoul 
~~~
새로 address와 order데이터를 추가한다. 
~~~
 bin/kafka-console-producer.sh --bootstrap-server my-kafka:9092 \
 --topic address \
 --property "parse.key=true" \
 --property "key.separator=:"
 
>namni:Jeju
~~~
~~~
bin/kafka-console-producer.sh --bootstrap-server my-kafka:9092 \
 --topic order \
 --property "parse.key=true" \
 --property "key.separator=:"
 
>namni:Tesla
~~~
신규 데이터를 확인한다.
~~~
bin/kafka-console-consumer.sh --bootstrap-server my-kafka:9092 \
 --topic order_join --from-beginning
 
>Tesla send to Jeju
~~~   



### KStreams와 GlobalKTable의 조인
토픽을 생성한다.  
address_v2라는 토픽을 생성하고 partition을 2개 생성하여 코파티셔닝 되지 않도록 한다.
~~~
bin/kafka-topics.sh --create --bootstrap-server my-kafka:9092 \
 --partitions 2 \
 --topic address_v2
~~~
KStreamJoinGlobalKTable main()함수를 실행한다.    
address_v2에 데이터를 추가한다
~~~
bin/kafka-console-producer.sh --bootstrap-server my-kafka:9092 \
 --topic address_v2 \
 --property "parse.key=true" \
 --property "key.separator=:"

>namni:Busan
~~~
order 데이터를 추가한다. 
~~~
bin/kafka-console-producer.sh --bootstrap-server my-kafka:9092 \
 --topic order \
 --property "parse.key=true" \
 --property "key.separator=:"

>namni:Tesla
~~~
신규 데이터를 확인한다.
~~~
bin/kafka-console-consumer.sh --bootstrap-server my-kafka:9092 \
 --topic order_join --from-beginning
~~~   

### 프로세서API
토픽을 생성한다(FilterStreams실습시 생성했다면 패쓰)
~~~
bin/kafka-topics.sh --bootstrap-server my-kafka:9092 --topic stream_log --create
bin/kafka-topics.sh --bootstrap-server my-kafka:9092 --topic stream_log_filter --create
~~~ 
SimpleProcessor main()함수를 실행한다.  
두 개의 창을 띄어 하나는 토픽을 추가하고 하나는 텍스트 길이가 5개 이상인 토픽이 저장되는 것을 확인한다.   
토픽에 데이터를 전송한다.
~~~
bin/kafka-console-producer.sh --bootstrap-server my-kafka:9092 --topic stream_log
~~~
메시지 값의 길이가 5개 초과인 데이터만 아래의 토픽에 저장된다.
~~~
bin/kafka-console-consumer.sh --bootstrap-server my-kafka:9092 --topic stream_log_filter
~~~
