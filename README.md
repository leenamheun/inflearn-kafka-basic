# Simple Kafka Producer

1. 카프카 콘솔 컨슈머를 실행할 수 있는 바이너리 디렉토리로 간다.
2. bin/kafka-server-start.sh config/server.properties 실행
3. SimpleProducer main()을 실행한다.
4. 콘솔을 통해 데이터가 잘 전송되었는지 확인 
   ~~~ 
   bin/kafka-console-consumer.sh --bootstrap-server my-kafka:9092 --topic test --from-beginning 
   ~~~